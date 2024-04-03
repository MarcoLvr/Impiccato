package me.marcolvr.commons.net.packetsout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.io.IOException;
import java.io.OutputStream;

public class PacketOutEndGame implements PacketOut{

    ByteBuf buf = Unpooled.buffer();
    boolean a;

    public PacketOutEndGame(boolean win){
        a=win;
        buf.writeByte(getPacketId());
        buf.writeBoolean(a);

    }

    @Override
    public int getPacketId() {
        return Packet.END_GAME.getId();
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
