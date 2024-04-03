package me.marcolvr.client.graphics;

import me.marcolvr.client.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Room extends JPanel {

    public Room(GridBagLayout gridBagLayout){
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        setOpaque(true);
        setBackground(new Color(173,38,38));
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 1;
        JLabel welcome = new JLabel("Benvenuto " + Main.username + "!");
        welcome.setFont(new Font("arial", Font.BOLD, 40));
        welcome.setForeground(Color.WHITE);
        add(welcome, constraints);

        constraints.gridy = 2;
        JLabel error = new JLabel("Partita già iniziata!");
        error.setFont(new Font("arial", Font.BOLD, 20));
        error.setForeground(Color.WHITE);
        error.setVisible(false);
        add(error, constraints);

        constraints.gridy = 3;
        JLabel marcolvr = new JLabel("Inserisci il codice della stanza");
        marcolvr.setFont(new Font("arial", Font.BOLD, 25));
        marcolvr.setForeground(Color.WHITE);
        add(marcolvr, constraints);

        constraints.gridy = 4;
        JTextField usernametext = new JTextField(16);
        usernametext.setFont(new Font("arial", Font.ITALIC, 18));
        add(usernametext, constraints);

        constraints.gridy = 5;
        JButton login = new JButton();
        login.setBackground(Color.GRAY);
        login.setFont(new Font("arial", Font.ITALIC, 18));
        login.setText("Gioca");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!Main.socketClientThread.tryRoom(usernametext.getText())){
                    error.setVisible(true);
                }else{
                    Main.socketClientThread.room = usernametext.getText();
                    Main.window.showWaiting(usernametext.getText());
                }
            }
        });
        add(login, constraints);
    }
}
