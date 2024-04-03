package me.marcolvr.client.graphics;

import me.marcolvr.client.Main;

import javax.swing.*;
import java.awt.*;

public class EndGame extends JPanel {

    public EndGame(GridBagLayout gridBagLayout, boolean win){
        Main.loc=1;
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        setOpaque(true);
        setBackground(new Color(173,38,38));
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 1;
        String txt = win ? "HAI VINTO!" : "HAI PERSO D:";
        JLabel welcome = new JLabel(txt);
        welcome.setFont(new Font("arial", Font.BOLD, 40));
        welcome.setForeground(Color.WHITE);
        add(welcome, constraints);

    }
}
