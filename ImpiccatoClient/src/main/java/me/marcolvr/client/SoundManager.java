package me.marcolvr.client;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class SoundManager {

    private static String[] musicFiles = new String[]{"calm1","calm2","calm3","piano1","piano2","piano3","nuance1","nuance2","hal1","hal2","hal3","hal4"};

    public static Clip playSound(String soundName) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        Clip clip = AudioSystem.getClip();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is =classloader.getResourceAsStream(soundName+".wav");
        InputStream bufferedIn = new BufferedInputStream(is);
        AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedIn);
        System.out.println("/"+soundName+".wav");
        clip.open(stream);
        clip.start();
        return clip;
    }

    public static void startMusic() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String randomSound = musicFiles[new Random().nextInt(musicFiles.length)];
        System.out.println(randomSound);
        Clip clip = AudioSystem.getClip();
        AudioInputStream stream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("/music/" +randomSound+".wav"));
        clip.open(stream);
        clip.addLineListener(event ->  {
            if(event.getType() == LineEvent.Type.STOP){
                try {
                    System.out.println("restart musica");
                    startMusic();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        clip.start();
    }
}
