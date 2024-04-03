package me.marcolvr.client.graphics;

import javax.swing.*;
import java.awt.*;

import me.marcolvr.client.Main;

public class WaitWord extends JPanel {

    public WaitWord(GridBagLayout gridBagLayout, String wordMaker){
        Main.loc=1;
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        setOpaque(true);
        setBackground(new Color(173,38,38));
        constraints.anchor = GridBagConstraints.CENTER;

        constraints.gridy = 1;
        JLabel welcome = new JLabel(wordMaker + " sta scegliendo la parola");
        welcome.setFont(new Font("arial", Font.BOLD, 40));
        welcome.setForeground(Color.WHITE);
        add(welcome, constraints);

    }
}
