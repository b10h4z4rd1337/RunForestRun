package com.forest.builder;

import com.forest.builder.render.BuilderPanel;
import com.forest.level.Level;
import com.forest.pc.PCMusicFactory;

import javax.swing.*;

/**
 * Created by user on 30.05.2016.
 */
public class Main {

    public static void main(String[] args) {
        Level.MUSIC_FACTORY = new PCMusicFactory();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("RunForestRunBuilder");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new BuilderPanel(frame));
            frame.pack();
            frame.setVisible(true);
        });
    }

}
