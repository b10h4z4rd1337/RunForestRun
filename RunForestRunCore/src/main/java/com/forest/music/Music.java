package com.forest.music;

/**
 * Created by user on 17.06.2016.
 */
public abstract class Music {

    public static MusicFactory MUSIC_FACTORY;

    private String musicName;

    public Music(String musicName) {
        this.musicName = musicName;
    }

    public abstract void start();
    public abstract void stop();
    public abstract void loop();

    public String getMusicName() {
        return musicName;
    }
}
