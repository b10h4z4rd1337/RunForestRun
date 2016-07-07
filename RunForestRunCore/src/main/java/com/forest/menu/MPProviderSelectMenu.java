package com.forest.menu;

import com.forest.net.MultiplayerProvider;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MPProviderSelectMenu extends Menu {

    public MPProviderSelectMenu(final Renderer renderer) {
        super("");

        int count = MultiplayerProvider.availableProviders.length;
        int completeHeight = 50 * count + 25 * (count - 1);
        int startY = renderer.getHeight() / 2 + completeHeight / 2;

        for (int i = 0; i < count; i++, startY -= 75) {
            final int copy = i;
            Button button = addButton(renderer.getWidth() / 2 - 100 / 2, startY, 100, 50, MultiplayerProvider.availableProviders[i].getName());
            button.setPressedCallback(new Button.Callback() {
                @Override
                public void onClick() {
                    MPModeMenu menu = new MPModeMenu(renderer, MultiplayerProvider.availableProviders[copy]);
                    renderer.clear();
                    renderer.addRenderable(menu);
                }
            });
        }
    }

}
