package me.marcolvr.commons.net.packetsout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.io.IOException;
import java.io.OutputStream;

public class PacketOutAddLetter implements PacketOut {

    ByteBuf buf = Unpooled.buffer();
    char a=0;

    public PacketOutAddLetter(char character){
        a=character;
        buf.writeByte(getPacketId());
    }

    public PacketOutAddLetter(){}

    @Override
    public int getPacketId() {
        return Packet.ADD_LETTER.getId();
    }

    @Override
    public ByteBuf getBuf() throws IOException {
        if(a==0){
            buf.writeByte(getPacketId());
            return buf.copy();
        }else{
            buf.writeChar(a);
            return buf.copy();
        }
    }

    @Override
    public boolean send(OutputStream out) {
        try{
            out.flush();
            //out.write(toByteArray());
            out.flush();
            return true;
        }catch (IOException e){
            return false;
        }
    }
}
