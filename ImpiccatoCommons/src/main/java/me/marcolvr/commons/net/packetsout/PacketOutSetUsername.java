package me.marcolvr.commons.net.packetsout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PacketOutSetUsername implements PacketOut {

    ByteBuf buf = Unpooled.buffer();
    String a;

    public PacketOutSetUsername(String username){
        a=username;
        buf.writeByte(getPacketId());
        buf.writeInt(a.length());
        buf.writeCharSequence(a, Charset.defaultCharset());
    }

    @Override
    public int getPacketId() {
        return Packet.SET_USERNAME.getId();
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
