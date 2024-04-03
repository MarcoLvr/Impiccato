package me.marcolvr.commons.net.packetsin;

import io.netty.buffer.ByteBuf;
import me.marcolvr.commons.LogEventType;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.nio.charset.Charset;

public class PacketInLogEvent implements PacketIn {

    LogEventType type;
    String a;
    char b;

    public PacketInLogEvent(ByteBuf buf) throws InvalidPacketException{
        type=LogEventType.getFromId(buf.readByte());
        switch (type){
            case RIGHT_LETTER -> {
                a=buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
                b=buf.readChar();
            }
            case WRONG_LETTER, PLAYER_EXIT -> {
                a=buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
            }
        }
        throw new InvalidPacketException();
    }

    public LogEventType getType(){
        return type;
    }

    public String getA() {
        return a;
    }

    public char getB(){
        return b;
    }

    @Override
    public int getPacketId() {
        return Packet.LOG_EVENT.getId();
    }

}
