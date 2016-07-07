package com.forest.android.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.forest.music.Music;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by b10h4 on 04.07.2016.
 */
public class AndroidMusic extends Music {

    public static Context CONTEXT;
    MediaPlayer player = new MediaPlayer();

    public AndroidMusic(String musicName) {
        super(musicName);
        try {
            AssetFileDescriptor afd = CONTEXT.getAssets().openFd(musicName);
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void stop() {
        player.stop();
    }

    @Override
    public void loop() {
        player.setLooping(true);
        player.start();
    }
}
