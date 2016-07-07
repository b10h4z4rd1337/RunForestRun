package com.forest.pc;

import com.forest.level.LevelData;
import com.forest.menu.LevelLoader;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Mathias on 04.07.2016.
 */
public class PCFileLoader extends LevelLoader {

    public static File appData = new File(getAppData() + "RunForestRun");

    public static String getAppData() {
        String path = "";
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN"))
            path = System.getenv("APPDATA");
        else if (OS.contains("MAC"))
            path = System.getProperty("user.home") + "/Library/";
        else if (OS.contains("NUX"))
            path = System.getProperty("user.home");
        else path =  System.getProperty("user.dir");

        path += "/";

        return path;
    }

    static {
        if (!appData.exists()) {
            appData.mkdir();
        }
    }

    @Override
    public String[] getAvailableLevels() {
        if (appData != null) {
            File[] files = appData.listFiles();
            if (files != null) {
                ArrayList<String> list = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile())
                        list.add(file.getName());
                }
                return list.toArray(new String[list.size()]);
            }
        }
        return null;
    }

    @Override
    public LevelData loadLevel(String name) throws IOException, ClassNotFoundException {
        File levelFile = new File(appData.getAbsolutePath() + "/" + name);
        FileInputStream fis = new FileInputStream(levelFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (LevelData) ois.readObject();
    }
}
