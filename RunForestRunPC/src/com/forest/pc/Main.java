package com.forest.pc;

import com.forest.pc.render.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("RunForestRun");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new GamePanel());
        frame.pack();
        frame.setVisible(true);
    }
}
