package me.marcolvr.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.EventExecutorGroup;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;

import java.nio.charset.Charset;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        System.out.println("Nuova connessione da " + ctx.channel().remoteAddress().toString());
        Client c = new Client(ctx.channel());
        GameManager.getClients().add(c);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Nuova disconnessione da " + ctx.channel().remoteAddress().toString());
        Client c = GameManager.getClients().stream().filter(client -> client.getAddress()==ctx.channel().remoteAddress()).findFirst().orElse(null);
        if(c!=null && c.getRoom() != null){
            c.getRoom().removePlayer(c);
            GameManager.unregister(c);
        }
        ctx.fireExceptionCaught(cause);
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Channel channel = channelHandlerContext.channel();
        ByteBuf buf = (ByteBuf) msg;
        Client c =GameManager.getClients().stream().filter(client -> client.getAddress() == channelHandlerContext.channel().remoteAddress()).findFirst().orElse(null);
        if(c.getUsername()==null){
            Packet packetId = Packet.getFromId(buf.readByte());
            if(packetId != Packet.SET_USERNAME){
                ByteBuf err = Unpooled.buffer(1);
                err.writeByte(Packet.ERROR.getId());
                channel.writeAndFlush(err);
                return;
            }
            int length = buf.readInt();
            String username =buf.readCharSequence(length, Charset.defaultCharset()).toString();
            if(GameManager.isUsernameUsed(username)){
                ByteBuf err = Unpooled.buffer(1);
                err.writeByte(Packet.INVALID.getId());
                channel.writeAndFlush(err);
                return;
            }
            c.setUsername(username);
            ByteBuf ok = Unpooled.buffer();
            ok.writeByte(Packet.OK.getId());
            channelHandlerContext.channel().writeAndFlush(ok);
            return;
        }
        if(c.getRoom() == null && buf.readByte()==Packet.SET_ROOM.getId()){
            int length = buf.readInt();
            String room = buf.readCharSequence(length, Charset.defaultCharset()).toString();
            if(!GameManager.roomExist(room)){
                ByteBuf ok = Unpooled.buffer();
                ok.writeByte(Packet.CREATED.getId());
                channelHandlerContext.channel().writeAndFlush(ok);
                c.setRoom(GameManager.createRoom(room, c));
                return;
            }
            if(GameManager.isStarted(room)){
                ByteBuf ok = Unpooled.buffer();
                ok.writeByte(Packet.INVALID.getId());
                channelHandlerContext.channel().writeAndFlush(ok);
            } else{
                ByteBuf ok = Unpooled.buffer();
                ok.writeByte(Packet.OK.getId());
                channelHandlerContext.channel().writeAndFlush(ok);
                c.setRoom(GameManager.joinRoom(room, c));
            }
        }else{
            c.incomingMessage(buf);
        }
    }
}
