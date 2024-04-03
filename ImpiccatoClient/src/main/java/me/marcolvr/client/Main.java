package me.marcolvr.client;

import io.netty.buffer.ByteBuf;
import me.marcolvr.client.graphics.GameWindow;
import me.marcolvr.client.graphics.MainWindow;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static final SocketClientThread socketClientThread = new SocketClientThread();

    public static MainWindow window;

    public static boolean arrived=false;
    public static ByteBuf buf;

    public static GameWindow game;

    public static int loc = 0;

    public static JLabel pass;

    public static String username;

    public static String ip = "";


    public static void main(String[] args){
        for (String arg: args) {
            if(arg.equalsIgnoreCase("-localhost")) ip="127.0.0.1";
            if(arg.startsWith("-serverip=")){
                ip=arg.substring(10);
            }
            if(arg.equalsIgnoreCase("-testGameWindow")) {
                window = new MainWindow();
                socketClientThread.room = "TEST";
                window.setVisible(true);
                window.showPanel(new GameWindow(new GridBagLayout(), 0, "TEST", "_ _ _ _ _ _ _ _ _ _ _", 0));
                return;
            }
        }
        window = new MainWindow();
        window.setVisible(true);
        socketClientThread.start();

    }
}


