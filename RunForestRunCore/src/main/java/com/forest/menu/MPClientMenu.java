package com.forest.menu;

import com.forest.net.MultiplayerProvider;
import com.forest.render.Renderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MPClientMenu extends Menu {

    private LinkedList<String> hosts = new LinkedList<>();

    public MPClientMenu(final Renderer renderer, final MultiplayerProvider provider) {
        super("");

        provider.discover(new MultiplayerProvider.HostFoundCallback() {
            @Override
            public void hostFound(final String host) {
                if (!hosts.contains(host)) {
                    hosts.add(host);

                    Button button = addButton(renderer.getWidth() / 2 - 200 / 2, 0, 200, 50, host);
                    button.setPressedCallback(new Button.Callback() {
                        @Override
                        public void onClick() {
                            provider.connect(host);
                            renderer.clear();
                            MPClientColorPickMenu menu = new MPClientColorPickMenu(renderer, provider);
                            renderer.addRenderable(menu);
                        }
                    });

                    List<Button> buttons = getButtons();
                    int count = buttons.size();
                    int startY = renderer.getHeight() / 2 + (count * 50 + count * 25) / 2;

                    for (Button b : buttons) {
                        b.setY(startY);
                        startY -= 75;
                    }
                }
            }
        });
    }

}
