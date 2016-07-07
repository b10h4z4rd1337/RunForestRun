package com.forest.menu;

import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.render.Renderer;

import java.io.IOException;

/**
 * Created by Mathias on 04.07.2016.
 */
public class SingleLevelSelectMenu extends Menu {
    public SingleLevelSelectMenu(final Renderer renderer) {
        super("");

        addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 100, 100, 50, "Random").setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                LevelData randomLevel = Level.createRandomLevel();
                renderer.clear();
                renderer.addRenderable(new Level(randomLevel));
            }
        });

        addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 50 / 2, 100, 50, "Test").setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                LevelData testLevel = Level.createTestLevel();
                renderer.clear();
                renderer.addRenderable(new Level(testLevel));
            }
        });

        addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 + 50, 100, 50, "Load").setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                LevelLoaderMenu menu = new LevelLoaderMenu(renderer, new LevelLoaderMenu.LevelSelectedCallback() {
                    @Override
                    public void levelSelected(String level) {
                        renderer.clear();
                        try {
                            renderer.addRenderable(new Level(LevelLoader.LOADER.loadLevel(level)));
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                renderer.clear();
                renderer.addRenderable(menu);
            }
        });
    }
}
