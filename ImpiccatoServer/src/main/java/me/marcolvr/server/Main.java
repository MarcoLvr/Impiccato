package me.marcolvr.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.marcolvr.commons.net.PacketEncoder;

import java.io.IOException;

public class Main {

    public static int starttime;

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length>0){
            try{
                starttime = Integer.parseInt(args[0]);
            }catch (Exception e){
                starttime = 30;
            }
        }else {
            starttime = 30;
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new PacketEncoder(),
                                    new ClientHandler());
                        }
                    });
            ChannelFuture start = server.bind(2463).sync();
            start.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

