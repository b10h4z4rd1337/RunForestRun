package com.forest.menu;

import com.forest.level.Level;
import com.forest.net.MultiplayerProvider;
import com.forest.render.Renderer;

import java.io.IOException;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MPHostLevelSelect extends Menu {

    public MPHostLevelSelect(final Renderer renderer, final MultiplayerProvider provider) {
        super("");
        addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 100, 100, 50, "Random").setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                renderer.clear();
                MPHostMenu hostMenu = new MPHostMenu(renderer, Level.createRandomLevel(), provider);
                renderer.addRenderable(hostMenu);
            }
        });

        addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 50 / 2, 100, 50, "Test").setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                renderer.clear();
                MPHostMenu hostMenu = new MPHostMenu(renderer, Level.createTestLevel(), provider);
                renderer.addRenderable(hostMenu);
            }
        });

        addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 + 50, 100, 50, "Load").setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                renderer.clear();
                LevelLoaderMenu levelLoaderMenu = new LevelLoaderMenu(renderer, new LevelLoaderMenu.LevelSelectedCallback() {
                    @Override
                    public void levelSelected(String level) {
                        renderer.clear();
                        try {
                            MPHostMenu hostMenu = new MPHostMenu(renderer, LevelLoader.LOADER.loadLevel(level), provider);
                            renderer.addRenderable(hostMenu);
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                renderer.addRenderable(levelLoaderMenu);
            }
        });
    }

}
