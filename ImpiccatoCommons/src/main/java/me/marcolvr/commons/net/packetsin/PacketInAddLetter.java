package me.marcolvr.commons.net.packetsin;

import io.netty.buffer.ByteBuf;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

public class PacketInAddLetter implements PacketIn {

    char a=0;

    public PacketInAddLetter(ByteBuf buf) throws InvalidPacketException{
        a=buf.readChar();
    }

    public char getA() {
        return a;
    }



    @Override
    public int getPacketId() {
        return Packet.ADD_LETTER.getId();
    }

}
