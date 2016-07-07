package com.forest.pc.music;

import com.forest.music.Music;
import com.forest.music.MusicFactory;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 17.06.2016.
 */
public class PCMusic extends Music {

    public static final MusicFactory FACTORY = new MusicFactory() {
        @Override
        public Music createMusic(String name) {
            return new PCMusic(name);
        }
    };

    private AudioStream audioStream;
    private boolean loopThis = false;

    public PCMusic(String musicName) {
        super(musicName);
    }

    @Override
    public void start() {
        new Thread(() -> {
            try {
                InputStream fis = Music.class.getResourceAsStream(getMusicName());
                audioStream = new AudioStream(fis);
                AudioPlayer.player.start(audioStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void loop() {
        loopThis = true;
        new Thread(() -> {
            try {
                start();
                while (audioStream.available() > 0) {
                    Thread.sleep(10);
                    if (audioStream == null)
                        break;
                }
                if (loopThis) {
                    loop();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stop() {
        loopThis = false;
        try {
            long unused = audioStream.skip(audioStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioPlayer.player.stop(audioStream);
    }
}
