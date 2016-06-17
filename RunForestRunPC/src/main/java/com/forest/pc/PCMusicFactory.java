package com.forest.pc;

import com.forest.music.Music;
import com.forest.music.MusicFactory;

/**
 * Created by user on 17.06.2016.
 */
public class PCMusicFactory extends MusicFactory {
    @Override
    public Music createMusic(String name) {
        return new PCMusic(name);
    }
}
