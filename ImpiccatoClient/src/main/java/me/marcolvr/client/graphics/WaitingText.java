package me.marcolvr.client.graphics;

import javax.swing.*;

public class WaitingText extends Thread{
    int loc = 0;
    JLabel text;

    public WaitingText(JLabel label){
        this.text = label;
    }

    public void run(){

        while (true){
            try {
                if(text.getText().startsWith("In attesa")){
                    switch (loc){
                        case 0:
                            text.setText("In attesa di giocatori.");
                            break;
                        case 1:
                            text.setText("In attesa di giocatori..");
                            break;
                        case 2:
                            text.setText("In attesa di giocatori...");
                            break;
                    }
                    loc++;
                    if(loc==3)loc=0;
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
