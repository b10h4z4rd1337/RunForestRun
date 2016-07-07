package com.forest.android.music;

import android.content.Context;

import com.forest.music.Music;
import com.forest.music.MusicFactory;

/**
 * Created by b10h4 on 04.07.2016.
 */
public class AndroidMusicFactory extends MusicFactory {

    public AndroidMusicFactory(Context context) {
        super();
        AndroidMusic.CONTEXT = context;
    }

    @Override
    public Music createMusic(String name) {
        return new AndroidMusic(name);
    }
}
