package com.forest.pc;

import com.forest.music.Music;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 17.06.2016.
 */
class PCMusic extends Music {

    private AudioStream audioStream;
    private boolean loopThis = false;

    PCMusic(String musicName) {
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
                    Thread.sleep(1);
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
            long ignored = audioStream.skip(audioStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioPlayer.player.stop(audioStream);
    }
}
