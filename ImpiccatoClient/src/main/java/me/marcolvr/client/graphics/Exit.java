package me.marcolvr.client.graphics;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import me.marcolvr.client.Main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Exit extends WindowAdapter {

    public void windowClosing(WindowEvent e){
            System.out.println("closing");
            Main.socketClientThread.shutdown();
            //Main.socketClientThread.workerGroup.shutdownGracefully();
        System.exit(0);

    }
}
