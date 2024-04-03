package me.marcolvr.commons.net.packetsout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.LobbyStatus;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PacketOutLobbyUpdate implements PacketOut{

    ByteBuf buf = Unpooled.buffer();
    LobbyStatus status;
    int a;
    int b;
    String c;
    String d;

    public LobbyStatus getStatus() {
        return status;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public PacketOutLobbyUpdate(int players){
        status=LobbyStatus.WAITING_FOR_PLAYERS;
        a=players;
        buf.writeByte(getPacketId());
        buf.writeByte(status.getId());
        buf.writeInt(a);
    }

    public PacketOutLobbyUpdate(int players, int time){
        status=LobbyStatus.STARTING;
        a=players;
        b=time;
        buf.writeByte(getPacketId());
        buf.writeByte(status.getId());
        buf.writeInt(a);
        buf.writeInt(b);
    }

    public PacketOutLobbyUpdate(int players, String wordMakerUsername, int tries, String word){
        status=LobbyStatus.SHOW_GAME_SCREEN;
        a=players;
        b=tries;
        c=wordMakerUsername;
        d=word;
        buf.writeByte(getPacketId());
        buf.writeByte(status.getId());
        buf.writeInt(a);
        buf.writeInt(b);
        buf.writeInt(c.length());
        buf.writeCharSequence(c, Charset.defaultCharset());
        buf.writeInt(d.length());
        buf.writeCharSequence(d, Charset.defaultCharset());
    }


    @Override
    public int getPacketId() {
        return Packet.LOBBY_UPDATE.getId();
    }


    @Override
    public ByteBuf getBuf()  throws IOException {
        return buf.copy();
    }
    @Override
    public boolean send(OutputStream out) {
        try{
            //out.write(toByteArray());
            out.flush();
            return true;
        }catch (IOException e){
            return false;
        }
    }
}
