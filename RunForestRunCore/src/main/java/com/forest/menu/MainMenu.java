package com.forest.menu;

import com.forest.render.Renderer;

/**
 * Created by Mathias on 23.05.16.
 */
public class MainMenu extends Menu {

    public MainMenu(final Renderer renderer) {
        super("");
        Button singlePlayerButton = addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 50, 100, 50, "Single");
        singlePlayerButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                SingleLevelSelectMenu menu = new SingleLevelSelectMenu(renderer);
                renderer.clear();
                renderer.addRenderable(menu);
            }
        });

        Button multiplayerButton = addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 + 50, 100, 50, "Multi");
        multiplayerButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                MPProviderSelectMenu mpModeMenu = new MPProviderSelectMenu(renderer);
                renderer.clear();
                renderer.addRenderable(mpModeMenu);
            }
        });
    }

}
