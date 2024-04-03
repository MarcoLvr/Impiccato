package me.marcolvr.commons.net.packetsin;

import io.netty.buffer.ByteBuf;
import me.marcolvr.commons.exceptions.InvalidPacketException;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

public class PacketInEndGame implements PacketIn {
    boolean a;


    public PacketInEndGame(ByteBuf buf) throws InvalidPacketException {
        a=buf.readBoolean();
    }

    public boolean getA() {
        return a;
    }


    @Override
    public int getPacketId() {
        return Packet.END_GAME.getId();
    }
}
