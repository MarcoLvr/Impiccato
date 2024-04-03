package me.marcolvr.commons.net.packetsout;


import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;

public interface PacketOut {


    int getPacketId();
    ByteBuf getBuf() throws IOException;
    boolean send(OutputStream out);
}
