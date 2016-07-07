package com.forest.menu;

import com.forest.input.Input;
import com.forest.level.LevelData;
import com.forest.level.Player;
import com.forest.level.Stopwatch;
import com.forest.level.block.Block;
import com.forest.net.MultiplayerLevel;
import com.forest.net.MultiplayerLocalHandle;
import com.forest.net.MultiplayerProvider;
import com.forest.net.MultiplayerServer;
import com.forest.render.Color;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MPHostMenu extends Menu {

    private ColorPicker colorPicker;

    public MPHostMenu(final Renderer renderer, final LevelData level, final MultiplayerProvider provider) {
        super("");

        int i = -1;
        for (Block block : level.blocks)
            block.setID(i++);

        final Player player = new Player(level.spawnPoint.x, level.spawnPoint.y, "player.png", null);
        final Stopwatch stopwatch = new Stopwatch();
        final MultiplayerServer server = new MultiplayerServer(provider, new MultiplayerLocalHandle(player), level, stopwatch);

        colorPicker = new ColorPicker(renderer.getWidth() / 2 - 170 / 2, renderer.getHeight() / 2 - 110 / 2, renderer, new ColorPicker.ColorPickedCallback() {
            @Override
            public void colorPicked(Color color) {
                player.setColor(color);
                server.setHostColor(color);
            }
        });

        Button start = addButton(renderer.getWidth() - 100, 0, 100, 50, "Start");
        start.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                MultiplayerLevel levelObj = new MultiplayerLevel(level, stopwatch, provider, server, 0);
                levelObj.setPlayer(player);
                player.setInpuAppliedCallback(new Player.InputAppliedCallback() {
                    @Override
                    public void inputApplied() {
                        if (player.isFirstMove())
                            server.allowMove();
                    }
                });
                player.setupInput(Input.FACTORY.createInput());
                server.start();
                renderer.clear();
                renderer.addRenderable(levelObj);
                renderer.addRenderable(server);
            }
        });
    }

    @Override
    public void render(Renderer renderer) {
        super.render(renderer);
        colorPicker.render(renderer);
    }

    @Override
    public void performClick(int x, int y) {
        super.performClick(x, y);
        colorPicker.performClick(x, y);
    }
}
