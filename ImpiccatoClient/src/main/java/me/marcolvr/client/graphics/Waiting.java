package me.marcolvr.client.graphics;

import me.marcolvr.client.Main;

import javax.swing.*;
import java.awt.*;

public class Waiting extends JPanel {

    public JLabel pls;
    public JLabel error;

    public Waiting(GridBagLayout gridBagLayout, String room) {
        Main.loc=0;
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        setOpaque(true);
        setBackground(new Color(173, 38, 38));
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 1;
        JLabel welcome = new JLabel("Codice stanza: " + room);
        welcome.setFont(new Font("arial", Font.BOLD, 40));
        welcome.setForeground(Color.WHITE);
        add(welcome, constraints);

        constraints.gridy = 2;
        pls = new JLabel("Giocatori: ");
        pls.setFont(new Font("arial", Font.BOLD, 40));
        pls.setForeground(Color.WHITE);
        add( pls, constraints);

        constraints.gridy = 3;
        error = new JLabel("In attesa di giocatori");
        error.setFont(new Font("arial", Font.BOLD, 20));
        error.setForeground(Color.WHITE);
        add(error, constraints);

        new WaitingText(error).start();

    }
}
