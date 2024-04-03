package me.marcolvr.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;

public class Client {

    @Getter @Setter
    private Room room;

    @Getter
    Channel ctx;

    @Getter @Setter
    private boolean messageArrived;

    @Getter
    private ByteBuf buf;

    @Getter @Setter
    public String username;

    @Getter
    public SocketAddress address;

    public Client(Channel cxt){
        this.ctx=cxt;
        this.address=ctx.remoteAddress();
        GameManager.getClients().add(this);
    }

    public void setWaitMessage(){
        messageArrived=false;
    }

    public void incomingMessage(ByteBuf buf){
        messageArrived=true;
        this.buf=buf;
    }

}
