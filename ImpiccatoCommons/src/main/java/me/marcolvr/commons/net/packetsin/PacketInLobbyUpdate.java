package me.marcolvr.commons.net.packetsin;

import io.netty.buffer.ByteBuf;
import me.marcolvr.commons.LobbyStatus;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.nio.charset.Charset;

public class PacketInLobbyUpdate implements PacketIn {

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

    public PacketInLobbyUpdate(ByteBuf buf) throws InvalidPacketException {
        byte stat = buf.readByte();
        switch (stat){
            case 0x01 -> {
                status = LobbyStatus.WAITING_FOR_PLAYERS;
                a = buf.readInt();
            }
            case 0x02 ->{
                status = LobbyStatus.STARTING;
                a = buf.readInt();
                b = buf.readInt();
            }
            case 0x03 ->{
                status= LobbyStatus.SHOW_GAME_SCREEN;
                a = buf.readInt();
                System.out.println(a);
                b = buf.readInt();
                System.out.println(b);
                c = buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
                System.out.println(c);
                d = buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
                System.out.println(d);
            }
        }
    }


    @Override
    public int getPacketId() {
        return status.getId();
    }
}
