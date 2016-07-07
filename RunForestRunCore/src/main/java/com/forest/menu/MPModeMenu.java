package com.forest.menu;

import com.forest.net.MultiplayerProvider;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MPModeMenu extends Menu {

    public MPModeMenu(final Renderer renderer, final MultiplayerProvider provider) {
        super("");
        Button hostButton = addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 - 50, 100, 50, "Host");
        hostButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                MPHostLevelSelect hostMenu = new MPHostLevelSelect(renderer, provider);
                renderer.clear();
                renderer.addRenderable(hostMenu);
            }
        });

        Button clientButton = addButton(renderer.getWidth() / 2 - 100 / 2, renderer.getHeight() / 2 + 50, 100, 50, "Client");
        clientButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                renderer.clear();
                MPClientMenu clientMenu = new MPClientMenu(renderer, provider);
                renderer.addRenderable(clientMenu);
            }
        });
    }

}
