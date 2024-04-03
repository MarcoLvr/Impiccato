package me.marcolvr.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import me.marcolvr.client.graphics.Username;
import me.marcolvr.client.graphics.LoadingText;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;
import me.marcolvr.commons.net.packetsout.PacketOutSetRoom;
import me.marcolvr.commons.net.packetsout.PacketOutSetUsername;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.Charset;

import static java.lang.System.out;

public class SocketClientThread extends Thread{

    public String room;
    public Channel ctx;
    public ChannelFuture f;
    public boolean messageArrived=false;
    private int tries = 0;

    public void arrived(ByteBuf buf){
        messageArrived=true;
    }


    public EventLoopGroup workerGroup = new NioEventLoopGroup();
    String[] regex = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

    public boolean tryUsername(String username){
        try{
            PacketOutSetUsername uname = new PacketOutSetUsername(username);
            ctx.writeAndFlush(uname.getBuf());
            Main.arrived=false;
            while (!Main.arrived){
                out.print("\rWaiting");}
            byte resp = Main.buf.readByte();
            return resp== Packet.OK.getId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean tryRoom(String room){
        try{
            PacketOutSetRoom uname = new PacketOutSetRoom(room);
            ctx.writeAndFlush(uname.getBuf());
            Main.arrived=false;
            while (!Main.arrived){
                out.print("\rWaiting");}
            byte resp = Main.buf.readByte();
            return resp!= Packet.ERROR.getId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;


    }

    public void run(){
        int tries = 0;
        Thread tt = new LoadingText();
        Main.window.connessione.setText("Connessione al server di gioco.");
        tt.start();
        out.println("Connessione al server di gioco");
        tries++;
        out.print("Tentativo numero " + tries + "\r");
        connect();

    }
    public void connect(){
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new PacketHandler(Main.socketClientThread));
            }
        });
        try {
            f = b.connect(Main.ip, 2463).sync();
            ctx=f.channel();
            out.println("Connesso ai server di gioco.");
            Main.window.connessione.setText("Connesso al server di gioco");
            Main.window.c2.setText("");
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Shutdown(ctx));
            Main.window.showPanel(new Username(new GridBagLayout()));
            out.println("Inserisci il tuo username");
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            tries++;
            if(tries==20){
                out.println("Impossibile connettersi al server di gioco\r");
                Main.window.connessione.setText("Impossibile connettersi al server di gioco");
                this.interrupt();
                e.printStackTrace();
                return;
            }
            out.println("tentativo " + tries);
            connect();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }

    public int shutdown() {
        try {
            f.channel().close().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}


class Shutdown extends Thread{
    Channel s;

    public Shutdown(Channel s){
        this.s = s;
    }


    public void run(){
        try {
            s.close().sync();
            Main.socketClientThread.workerGroup.shutdownGracefully();
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
