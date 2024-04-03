package me.marcolvr.client.graphics;

import io.netty.buffer.ByteBuf;
import me.marcolvr.client.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.PacketUtils;
import me.marcolvr.commons.net.packetsout.PacketOutSetWord;

public class SetWord extends JPanel {

    public SetWord(GridBagLayout gridBagLayout){
        Main.loc=1;
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        setOpaque(true);
        setBackground(new Color(173,38,38));
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 1;
        JLabel error = new JLabel("Parola non valida");
        error.setFont(new Font("arial", Font.BOLD, 20));
        error.setForeground(Color.WHITE);
        error.setVisible(false);
        add(error, constraints);

        constraints.gridy = 2;
        JLabel marcolvr = new JLabel("Sei il wordMaker! Scegli una parola senza spazi");
        marcolvr.setFont(new Font("arial", Font.BOLD, 25));
        marcolvr.setForeground(Color.WHITE);
        add(marcolvr, constraints);

        constraints.gridy = 3;
        JTextField usernametext = new JTextField(16);
        usernametext.setFont(new Font("arial", Font.ITALIC, 18));
        add(usernametext, constraints);

        constraints.gridy = 4;
        JButton login = new JButton();
        login.setBackground(Color.GRAY);
        login.setFont(new Font("arial", Font.ITALIC, 18));
        login.setText("Conferma");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(usernametext.getText().indexOf(' ') != -1){
                        return;
                    }
                    PacketOutSetWord word = new PacketOutSetWord(usernametext.getText());
                    Main.socketClientThread.ctx.writeAndFlush(word.getBuf());
                    System.out.println("inviato");
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        });
        add(login, constraints);
    }
}
