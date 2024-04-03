package me.marcolvr.commons.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;
import me.marcolvr.commons.net.packetsout.PacketOut;

import java.util.List;

public class PacketEncoder extends MessageToByteEncoder<PacketOut> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, PacketOut packetOut, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(packetOut.getBuf().array());
    }
}
