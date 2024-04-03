package me.marcolvr.client.graphics;

import me.marcolvr.client.Main;

public class LoadingText extends Thread{

    int loc = 0;

    public void run(){
        while (true){
            try {
                if(Main.window.connessione.getText().startsWith("Connessione")){
                    switch (loc){
                        case 0:
                            Main.window.connessione.setText("Connessione al server di gioco.");
                            break;
                        case 1:
                            Main.window.connessione.setText("Connessione al server di gioco..");
                            break;
                        case 2:
                            Main.window.connessione.setText("Connessione al server di gioco...");
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
