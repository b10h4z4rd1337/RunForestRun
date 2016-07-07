package com.forest.menu;

import com.forest.level.LevelData;

import java.io.IOException;

/**
 * Created by Mathias on 04.07.2016.
 */
public abstract class LevelLoader {

    public static LevelLoader LOADER;

    public abstract String[] getAvailableLevels();
    public abstract LevelData loadLevel(String name) throws IOException, ClassNotFoundException;

}
