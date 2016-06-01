package com.forest.builder;

import com.forest.level.LevelData;
import com.sun.istack.internal.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;

/**
 * Created by Mathias on 31.05.16.
 */
public class Utils {

    public static void levelDataToFile(LevelData levelData, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(levelData);
    }

    public static LevelData fileToLevelData(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (LevelData) ois.readObject();
    }

    private static JFileChooser createFileChooser() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isFile() && f.getName().endsWith(".rfr");
            }

            @Override
            public String getDescription() {
                return "RunForestRun Levels (.rfr)";
            }
        });
        return fileChooser;
    }

    @Nullable
    public static File openFileOpenDialog(Component parent) {
        final JFileChooser fileChooser = createFileChooser();
        int retVal = fileChooser.showOpenDialog(parent);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    @Nullable
    public static File openFileSaveDialog(Component parent) {
        final JFileChooser fileChooser = createFileChooser();
        int retVal = fileChooser.showSaveDialog(parent);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

}
