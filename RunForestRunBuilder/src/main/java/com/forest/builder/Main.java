package com.forest.builder;

import com.forest.builder.render.BuilderPanel;

import javax.swing.*;

/**
 * Created by user on 30.05.2016.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("RunForestRunBuilder");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new BuilderPanel(frame));
            frame.pack();
            frame.setVisible(true);
        });
    }

}
