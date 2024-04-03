package me.marcolvr.client.graphics;

import me.marcolvr.client.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {

    public JLabel connessione;
    public JLabel c2;
    public Waiting waiting;

    public MainWindow(){
        super("Impiccato v1.0 by MarcoLvr");
        setSize(1280,720);
        setResizable(false);
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new Exit());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(173,38,38));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridy = 1;
        JLabel marcolvr = new JLabel("Impiccato");
        marcolvr.setFont(new Font("arial", Font.BOLD, 100));
        marcolvr.setForeground(Color.WHITE);
        panel.add(marcolvr, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        connessione = new JLabel();
        connessione.setForeground(Color.WHITE);
        connessione.setFont(new Font("arial", Font.PLAIN, 25));
        panel.add(connessione, constraints);

        constraints.gridy = 3;
        c2 = new JLabel();
        c2.setForeground(Color.WHITE);
        c2.setFont(new Font("arial", Font.ITALIC, 18));
        panel.add(c2, constraints);

        add(panel);
    }

    public void showWaiting(String room){
        waiting = new Waiting(new GridBagLayout(), room);
        getContentPane().removeAll();
        add(waiting);
        validate();
    }

    public void showPanel(JPanel panel){
        getContentPane().removeAll();
        add(panel);
        validate();
    }

}
