package com.forest.builder;

import com.forest.builder.render.BuilderPanel;
import com.forest.music.Music;
import com.forest.pc.music.PCMusic;

import javax.swing.*;

/**
 * Created by user on 30.05.2016.
 */
public class Main {

    public static void main(String[] args) {
        Music.MUSIC_FACTORY = PCMusic.FACTORY;

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("RunForestRunBuilder");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new BuilderPanel(frame));
            frame.pack();
            frame.setFocusable(true);
            frame.requestFocusInWindow();
            frame.setVisible(true);
        });
    }

}
