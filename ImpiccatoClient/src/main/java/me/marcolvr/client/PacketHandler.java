package me.marcolvr.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.marcolvr.client.graphics.*;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.packetsin.PacketInEndGame;
import me.marcolvr.commons.net.packetsin.PacketInLobbyUpdate;
import me.marcolvr.commons.net.packetsin.PacketInUpdateWord;
import me.marcolvr.commons.net.packetsin.PacketInWaitWord;

import java.awt.*;

import static me.marcolvr.client.SoundManager.playSound;

public class PacketHandler extends ChannelInboundHandlerAdapter {

    private SocketClientThread client;

    public PacketHandler(SocketClientThread c){
        client=c;
    }


    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Channel channel = channelHandlerContext.channel();
        ByteBuf buf = (ByteBuf) msg;
        Packet packet = Packet.getFromId(buf.readByte());
        System.out.println("Received packet with id: " + packet + " size: " + buf.readableBytes());
        if (packet == null) return;
        switch (packet) {
            case HEARTBEAT:
                break;
            case LOBBY_UPDATE:
                if (Main.loc > 0) {
                    Main.window.showWaiting(Main.username);
                }
                PacketInLobbyUpdate lu = new PacketInLobbyUpdate(buf);
                switch (lu.getPacketId()) {
                    case 0x01:
                        Main.window.waiting.pls.setText("Giocatori: " + lu.getA());
                        if (!Main.window.waiting.error.getText().startsWith("In attesa"))
                            Main.window.waiting.error.setText("In attesa di giocatori");
                        break;
                    case 0x02:
                        Main.window.waiting.pls.setText("Giocatori: " + lu.getA());
                        Main.window.waiting.error.setText("Inizio in " + lu.getB());
                        if(lu.getB()<=10) playSound("pling");
                        break;
                    case 0x03:
                        Main.game = new GameWindow(new GridBagLayout(), lu.getA(), lu.getC(), lu.getD(), lu.getB());
                        Main.window.showPanel(Main.game);
                        break;
                }
                break;
            case SET_WORD:
                playSound("levelup");
                Main.window.showPanel(new SetWord(new GridBagLayout()));
                break;
            case WAIT_WORD:
                playSound("levelup");
                PacketInWaitWord word = new PacketInWaitWord(buf);
                Main.window.showPanel(new WaitWord(new GridBagLayout(), word.getA()));
                break;
            case ADD_LETTER:
                Main.game.chartext.setVisible(true);
                break;
            case END_GAME:
                PacketInEndGame endGame = new PacketInEndGame(buf);
                if (endGame.getA()) {
                    playSound("win");
                }
                Main.window.showPanel(new EndGame(new GridBagLayout(), endGame.getA()));
                Thread.sleep(4500);
                Main.window.showWaiting(Main.socketClientThread.room);
                break;
            case UPDATE_WORD:
                PacketInUpdateWord updateWord = new PacketInUpdateWord(buf);
                String w = updateWord.getA();
                String wordSpaced = String.valueOf(w.charAt(0));
                for (int i = 1; i<w.length();i++){
                    wordSpaced+=" "+w.charAt(i);
                }
                Main.game.parola.setText(wordSpaced);
                if(Integer.parseInt(Main.game.tent.getText())>updateWord.getB()){
                    playSound("bass");
                }else{
                    playSound("harp");
                }
                Main.game.tent.setText(String.valueOf(updateWord.getB()));
                break;
            case OK:
            case CREATED:
            case ERROR:
            case INVALID:
                buf.writeByte(packet.getId());
                Main.buf = buf;
                Main.arrived=true;
        }
    }
}