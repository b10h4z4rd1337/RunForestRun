package com.forest.menu;

import com.forest.render.Renderer;

/**
 * Created by Mathias on 04.07.2016.
 */
public class LevelLoaderMenu extends Menu {

    public LevelLoaderMenu(final Renderer renderer, final LevelSelectedCallback levelSelectedCallback) {
        super("");

        final String[] levels = LevelLoader.LOADER.getAvailableLevels();
        if (levels.length != 0) {
            int count = levels.length;
            int height = count * 50 + (count - 1) * 25;

            for (int i = 0, y = renderer.getHeight() / 2 + height / 2; y > renderer.getHeight() / 2 - height / 2; y -= 75, i++) {
                final int pos = i;
                addButton(renderer.getWidth() / 2 - 100 / 2, y, 100, 50, levels[pos].replace(".rfr", "")).setPressedCallback(new Button.Callback() {
                    @Override
                    public void onClick() {
                        levelSelectedCallback.levelSelected(levels[pos]);
                    }
                });
            }
        }
    }

    public interface LevelSelectedCallback {
        void levelSelected(String level);
    }

}
