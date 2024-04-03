package me.marcolvr.client.graphics;

import me.marcolvr.client.Main;
import me.marcolvr.commons.net.Packet;
import me.marcolvr.commons.net.packetsout.PacketOutAddLetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameWindow extends JPanel {

    public JTextField chartext;
    public JLabel parola, tent;

    public GameWindow(GridBagLayout gridBagLayout, int pls, String wordmaker, String word, int tentativi){
        this.setLayout(null);

        setOpaque(true);
        setBackground(new Color(173,38,38));


        JLabel welcome = new JLabel("Stanza: "+ Main.socketClientThread.room.toUpperCase());
        welcome.setFont(new Font("arial", Font.BOLD, 25));
        welcome.setForeground(Color.WHITE);
        add(welcome);
        Insets insets = this.getInsets();
        welcome.setBounds(10+insets.top, 10+insets.left, welcome.getPreferredSize().width, welcome.getPreferredSize().height);

        JLabel players = new JLabel("Giocatori: " + pls);
        players.setFont(new Font("arial", Font.BOLD, 25));
        players.setForeground(Color.WHITE);
        players.setVisible(true);
        add(players);
        players.setBounds(1090+insets.top, 10+insets.left, players.getPreferredSize().width,players.getPreferredSize().height);

        String wordSpaced = String.valueOf(word.charAt(0));
        for (int i = 1; i<word.length();i++){
            wordSpaced+=" "+word.charAt(i);
        }
        parola = new JLabel(wordSpaced);
        parola.setFont(new Font("arial", Font.BOLD, 40));
        parola.setForeground(Color.WHITE);
        add(parola);
        parola.setBounds(1280/2-parola.getPreferredSize().width/2+insets.top, 250+insets.left, parola.getPreferredSize().width+20,parola.getPreferredSize().height);



        chartext = new JTextField(1);
        chartext.setFont(new Font("arial", Font.ITALIC, 40));
        chartext.setVisible(false);
        chartext.setDocument(new JTextFieldLimit(1));
        chartext.addActionListener(e -> {
            PacketOutAddLetter l = new PacketOutAddLetter(chartext.getText().charAt(0));
            try {
                Main.socketClientThread.ctx.writeAndFlush(l.getBuf());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            chartext.setVisible(false);
            chartext.setText("");
        });
        add(chartext);
        chartext.setBounds(1280/2-chartext.getPreferredSize().width/2+insets.top, 350+insets.left, chartext.getPreferredSize().width,chartext.getPreferredSize().height);

        JLabel tentr = new JLabel("Tentativi rimasti");
        tentr.setFont(new Font("arial", Font.PLAIN, 30));
        tentr.setVisible(true);
        tentr.setForeground(Color.WHITE);
        add(tentr);
        tentr.setBounds(1280/2-tentr.getPreferredSize().width/2+insets.top, 450+insets.left, tentr.getPreferredSize().width,tentr.getPreferredSize().height);

        tent = new JLabel(String.valueOf(tentativi));
        tent.setFont(new Font("arial", Font.ITALIC, 30));
        tent.setVisible(true);
        tent.setForeground(Color.WHITE);
        add(tent);
        tent.setBounds(1280/2-tent.getPreferredSize().width/2+insets.top, 500+insets.left, tent.getPreferredSize().width,tent.getPreferredSize().height);
    }
}
