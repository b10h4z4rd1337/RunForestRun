package com.forest.menu;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 23.05.16.
 */
public class MainMenu extends Menu {

    public MainMenu(final Renderer renderer) {
        super("");
        Button startButton = addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 50 / 2, 100, 50, "Start");
        startButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                Level testLevel = new Level(Level.createTestLevel());
                renderer.clear();
                renderer.addRenderable(testLevel);
            }
        });
    }

}
