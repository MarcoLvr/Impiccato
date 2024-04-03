package me.marcolvr.commons.net.packetsin;

import io.netty.buffer.ByteBuf;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.nio.charset.Charset;

public class PacketInUpdateWord implements PacketIn {

    String a;
    int b;

    public PacketInUpdateWord(ByteBuf buf) throws InvalidPacketException {
        a=buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
        b=buf.readInt();
    }

    public String getA(){
        return a;
    }

    public int getB(){
        return b;
    }

    @Override
    public int getPacketId() {
        return Packet.UPDATE_WORD.getId();
    }
}
