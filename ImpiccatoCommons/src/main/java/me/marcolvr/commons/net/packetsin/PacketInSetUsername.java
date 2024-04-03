package me.marcolvr.commons.net.packetsin;

import io.netty.buffer.ByteBuf;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.nio.charset.Charset;

public class PacketInSetUsername implements PacketIn {

    String a;

    public PacketInSetUsername(ByteBuf buf) throws InvalidPacketException {
        a=buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
    }

    public String getA(){
        return a;
    }

    @Override
    public int getPacketId() {
        return Packet.SET_USERNAME.getId();
    }
}