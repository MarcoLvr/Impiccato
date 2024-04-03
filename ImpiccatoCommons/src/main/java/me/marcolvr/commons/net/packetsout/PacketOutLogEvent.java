package me.marcolvr.commons.net.packetsout;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.marcolvr.commons.LogEventType;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class PacketOutLogEvent implements PacketOut {

    ByteBuf buf = Unpooled.buffer();
    LogEventType type;
    String a;
    char b;

    public PacketOutLogEvent(LogEventType type){
        this.type = type;
    }

    public PacketOutLogEvent(LogEventType type, String username){
        this.type = type;
        a = username;
        buf.writeByte(getPacketId());
        buf.writeByte(type.getId());
        buf.writeInt(a.length());
        buf.writeCharSequence(a, Charset.defaultCharset());
    }

    public PacketOutLogEvent(LogEventType type, String username, char letter){
        this.type = type;
        a = username;
        b = letter;
        buf.writeByte(getPacketId());
        buf.writeByte(type.getId());
        buf.writeInt(a.length());
        buf.writeCharSequence(a, Charset.defaultCharset());
        buf.writeChar(b);
    }

    @Override
    public int getPacketId() {
        return Packet.LOG_EVENT.getId();
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
