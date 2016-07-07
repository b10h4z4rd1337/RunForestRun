package com.forest.menu;

import com.forest.level.Player;
import com.forest.level.Stopwatch;
import com.forest.net.MultiplayerClient;
import com.forest.net.MultiplayerLevel;
import com.forest.net.MultiplayerPacket;
import com.forest.net.MultiplayerProvider;
import com.forest.render.Color;
import com.forest.render.Renderer;
import org.jbox2d.common.Vec2;

/**
 * Created by user on 01.07.2016.
 */
public class MPClientColorPickMenu extends Menu {

    private ColorPicker colorPicker;

    public MPClientColorPickMenu(final Renderer renderer, final MultiplayerProvider provider) {
        super("");

        final MultiplayerClient client = new MultiplayerClient(provider, new MultiplayerClient.ClientReadyCallback() {
            @Override
            public void clientReady(final MultiplayerClient client) {

                final Player player = new Player(client.getLevelData().spawnPoint.x, client.getLevelData().spawnPoint.y, "player.png", null);
                client.setPlayer(player);

                player.setPositionChangedCallback(new Player.PositionChangedCallback() {
                    @Override
                    public void positionChanged() {
                        MultiplayerPacket packet = new MultiplayerPacket();
                        packet.setOption(MultiplayerPacket.POS);
                        packet.setIndex(client.getIndex());
                        Vec2 pos = player.getBody().getPosition();
                        packet.setPos(pos.x, pos.y);
                        provider.send(packet.constructPacket());
                    }
                });

                MultiplayerLevel level = new MultiplayerLevel(client.getLevelData(), new Stopwatch() {
                    @Override
                    public long getTime() {
                        return client.getTime();
                    }
                }, provider, null, client.getIndex());
                level.setPlayer(player);
                renderer.clear();
                renderer.addRenderable(level);
                renderer.addRenderable(client);
            }
        });

        colorPicker = new ColorPicker(renderer.getWidth() / 2 - 170 / 2, renderer.getHeight() / 2 - 110 / 2, renderer, new ColorPicker.ColorPickedCallback() {
            @Override
            public void colorPicked(Color color) {
                client.setColor(color);
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
