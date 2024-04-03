package me.marcolvr.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.LogEventType;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;
import me.marcolvr.commons.net.packetsin.PacketInAddLetter;
import me.marcolvr.commons.net.packetsin.PacketInSetWord;
import me.marcolvr.commons.net.packetsout.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Room extends Thread{

    ArrayList<Client> clients = new ArrayList<>();
    public String word;
    public String iword = "";
    private Client sel;
    public final int starttime = Main.starttime;
    private int currentTime = starttime;
    public final String code;
    boolean started = false;
    public int step = 0;
    public int tries = 10;

    public Collection<Client> getCurrentClients(){
        List<Client> current = (List<Client>) clients.clone();
        return Collections.unmodifiableCollection(current);
    }

    public Room addPlayer(Client client){
        clients.add(client);
        System.out.println(String.format("[%s] %s joined.", code, client.username));
        try {
            sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updatePlayers();
        if (clients.size() >= 2 && step == 0) {
            step = 1;
        }
        return this;
    }

    public void removePlayer(Client client){
        clients.remove(client);
        System.out.println(String.format("[%s] %s left.", code, client.username));
        if(step>=1){
            reset();
        }
        if(step>1 && client == sel){
            sel = null;
            iword = "";
            word = null;
            tries=10;
            step = 1;
        }
        updatePlayers();
    }

    public void reset(){
        iword = "";
        word = null;
        tries=10;
        currentTime=starttime;
        started = false;
        step = 0;
        if (clients.size() >= 2) {
            step = 1;
        }
        updatePlayers();
    }

    public Room(Client client, String code){
        this.code = code.toUpperCase(Locale.ROOT);
        addPlayer(client);
        this.start();
        System.out.println(String.format("[%s] Room created.", code));

    }

    private void updatePlayers() {
        PacketOutLobbyUpdate lobbyUpdate = new PacketOutLobbyUpdate(clients.size());
        Collection<Client> currentClients = getCurrentClients();
        for (Client c : currentClients){
            sendPacket(c, lobbyUpdate);
        }
    }

    private void sendPacket(Client c, PacketOut packet) {
        try {
            c.getCtx().writeAndFlush(packet.getBuf());
            System.out.println(String.format("[%s] Successfully sent %s to %s",code,Packet.getFromId(packet.getPacketId()).toString(),c.username));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void run() {
        while (true) {
            //EXEC ONE INSTRUCTION-SET PER SECOND
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //DELETE ROOM IF EMPTY
            if (clients.size() == 0) {
                System.out.println(String.format("[%s] Room empty. Deleting it", code));
                GameManager.deleteRoom(this);
                break;
            }
            List<Client> remove;
            switch (step){
                case 1:
                    //STATUS CHANGE TO 0 WHEN THERE ISN'T ENOUGH PLAYERS
                    if(clients.size() < 2){
                        currentTime = starttime;
                        step = 0;
                        System.out.println(String.format("[%s] Status changed to 0", code));
                        remove = new ArrayList<>();
                        PacketOutLobbyUpdate updateClients = new PacketOutLobbyUpdate(clients.size());
                        for (Client c : clients) {
                            try {
                                c.getCtx().writeAndFlush(updateClients.getBuf());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        for (Client c : remove) {
                            removePlayer(c);
                            GameManager.unregister(c);
                        }
                        continue;
                    }
                    if(currentTime > 0){
                        //GAME START COUNTDOWN
                        PacketOutLobbyUpdate updateTimer  = new PacketOutLobbyUpdate(clients.size(), currentTime);
                        getCurrentClients().forEach(client -> {
                            sendPacket(client, updateTimer);
                        });
                        currentTime--;
                        continue;
                    }
                    //GAME START
                    if(currentTime==0){
                        step = 2;
                        started=true;
                        sel = clients.get(new Random().nextInt(clients.size()));
                        PacketOutWaitWord waitWord =new PacketOutWaitWord(sel.username);
                        PacketOutSetWord w = new PacketOutSetWord("NULL");
                        getCurrentClients().forEach(client ->{
                            if(client==sel) sendPacket(sel, w);
                            else sendPacket(client, waitWord);
                        });
                        while (!sel.isMessageArrived()){
                            if(!GameManager.getClients().contains(sel)){
                                reset();
                                break;
                            }else{
                                System.out.print(String.format("[%s] Waiting for WordMaker\r", code));;
                            }
                        }
                        sel.setMessageArrived(false);
                        try{
                            System.out.println("arrivato");
                            ByteBuf buf = sel.getBuf();
                            Packet p = Packet.getFromId(buf.readByte());
                            if (p == Packet.SET_WORD) {
                                PacketInSetWord word = new PacketInSetWord(buf);
                                this.word = word.getA();
                                for (int i = 0; i < word.getA().length(); i++) {
                                    iword += "_";
                                }
                                PacketOutLobbyUpdate started = new PacketOutLobbyUpdate(clients.size(), sel.username, tries, iword);
                                getCurrentClients().forEach(client ->{
                                    sendPacket(client, started);
                                });
                            }else{
                                removePlayer(sel);
                                step=1;
                                currentTime=0;
                                GameManager.unregister(sel);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        continue;
                    }
                case 2:
                    for(Client client : getCurrentClients()){
                        if(client==sel) continue;
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        PacketOutAddLetter letter = new PacketOutAddLetter();
                        sendPacket(client, letter);
                        while (!client.isMessageArrived()){
                            if(!GameManager.getClients().contains(client)){
                                continue;
                            }
                            System.out.print(String.format("[%s] Waiting for client\r", code));;
                        }
                        client.setMessageArrived(false);
                        try{
                            Packet p = Packet.getFromId(client.getBuf().readByte());
                            PacketInAddLetter l = new PacketInAddLetter(client.getBuf());
                            if(!word.contains(""+l.getA())){
                                PacketOutLogEvent fail = new PacketOutLogEvent(LogEventType.WRONG_LETTER, client.username);
                                if(tries==0){
                                    System.out.println(String.format("[%s] fine gioco", code));
                                    PacketOutEndGame wordMaker = new PacketOutEndGame(true);
                                    PacketOutEndGame end = new PacketOutEndGame(false);
                                    for (Client c : getCurrentClients()){
                                        if(c==sel){
                                            sendPacket(c, wordMaker);
                                        }else {
                                            sendPacket(c, end);
                                        }
                                    }
                                    Thread.sleep(5000);
                                    step=0;
                                    reset();
                                    break;
                                }
                                tries--;
                                System.out.println(String.format("[%s] %s non ha indovinato la lettera", code, client.username));
                                PacketOutUpdateWord remTry = new PacketOutUpdateWord(iword, tries);
                                getCurrentClients().forEach(c ->{
                                    sendPacket(c, remTry);
                                });
                            }else{
                                PacketOutLogEvent success = new PacketOutLogEvent(LogEventType.RIGHT_LETTER, client.username, l.getA());
                                char[] wordSectioned = word.toCharArray();
                                char[] iwordSectioned = iword.toCharArray();
                                String neww = iword = "";
                                for (int i = 0; i<wordSectioned.length; i++){
                                    if(wordSectioned[i]==l.getA()){
                                        neww+=l.getA();
                                    }else
                                        neww+=iwordSectioned[i];
                                }
                                iword=neww;
                                System.out.println(String.format("[%s] %s ha indovinato una lettera", code, client.username));
                                if(!iword.contains("_")){
                                    System.out.println(String.format("[%s] fine gioco", code));
                                    PacketOutEndGame end = new PacketOutEndGame(true);
                                    PacketOutEndGame wordMaker = new PacketOutEndGame(false);
                                    for (Client c : getCurrentClients()){
                                        if(c==sel){
                                            sendPacket(c, wordMaker);
                                        }else {
                                            sendPacket(c, end);
                                        }
                                    }
                                    Thread.sleep(5000);
                                    step=0;
                                    reset();
                                    break;
                                }
                                PacketOutUpdateWord wordUpd = new PacketOutUpdateWord(iword, tries);
                                getCurrentClients().forEach(c ->{
                                    sendPacket(c, wordUpd);
                                });
                            }
                        }catch (InvalidPacketException e){
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    }
}