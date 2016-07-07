package com.forest.pc;

import com.forest.input.Input;
import com.forest.input.InputFactory;
import com.forest.menu.LevelLoader;
import com.forest.music.Music;
import com.forest.net.MultiplayerProvider;
import com.forest.pc.input.PCInput;
import com.forest.pc.music.PCMusic;
import com.forest.pc.net.PCNetworkProvider;
import com.forest.pc.render.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final GamePanel panel = new GamePanel(null, false);
        MultiplayerProvider.availableProviders = new MultiplayerProvider[] { new PCNetworkProvider() };
        Music.MUSIC_FACTORY = PCMusic.FACTORY;
        LevelLoader.LOADER = new PCFileLoader();
        Input.FACTORY = new InputFactory() {

            private PCInput pcInput = null;

            @Override
            public Input createInput() {
                if (pcInput == null) {
                    pcInput = new PCInput();
                    panel.addKeyListener(pcInput);
                }
                return pcInput;
            }
        };
        JFrame frame = new JFrame("RunForestRun");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
