package me.marcolvr.commons.net.packetsout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PacketOutSetRoom implements PacketOut {

    ByteBuf buf = Unpooled.buffer();
    String a;

    public PacketOutSetRoom(String room){
        a=room;
        buf.writeByte(getPacketId());
        buf.writeInt(room.length());
        buf.writeCharSequence(room, Charset.defaultCharset());
    }

    @Override
    public int getPacketId() {
        return Packet.SET_ROOM.getId();
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
