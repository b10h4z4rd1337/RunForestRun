package com.forest.net;

import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.level.Player;
import com.forest.level.Stopwatch;
import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by user on 24.06.2016.
 */
public class MultiplayerServer implements Renderable {

    private static final String START_PACKET = new MultiplayerPacket(MultiplayerPacket.START).constructPacket();

    private final MultiplayerProvider provider;
    private final AtomicBoolean continueRunning = new AtomicBoolean(true);
    private final LinkedList<MultiplayerHandle> handles = new LinkedList<>();
    private final MultiplayerPacket timePacket = new MultiplayerPacket(), goPacket = new MultiplayerPacket();
    private final Stopwatch stopwatch;
    private volatile ConcurrentLinkedQueue<String> blocksToRemove = new ConcurrentLinkedQueue<>();

    public MultiplayerServer(MultiplayerProvider provider, MultiplayerHandle localHandle, final LevelData levelData, Stopwatch stopwatch) {
        this.provider = provider;
        this.handles.add(localHandle);
        this.stopwatch = stopwatch;
        this.timePacket.setOption(MultiplayerPacket.TIME);
        this.goPacket.setOption(MultiplayerPacket.GO);

        provider.startCollect(new ClientFoundCallback() {
            @Override
            public void clientFound(MultiplayerHandle handle) {
                handle.setServer(MultiplayerServer.this);
                handles.add(handle);

                MultiplayerPacket packet = new MultiplayerPacket();
                packet.setOption(MultiplayerPacket.LEVEL);
                try {
                    packet.setData(MultiplayerUtils.levelDataToString(levelData));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handle.send(packet.constructPacket());

                packet = new MultiplayerPacket();
                packet.setOption(MultiplayerPacket.INDEX);
                packet.setIndex(handles.indexOf(handle));
                handle.send(packet.constructPacket());

                //TODO: Update GUI
            }
        });
    }

    public void start() {

        for (MultiplayerHandle handle : handles)
            if (handle.getLastData().color == null)
                return;

        provider.endCollect();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (continueRunning.get()) {
                    StringBuilder update = new StringBuilder();
                    for (MultiplayerHandle input : handles) {
                        MultiplayerPacket inPacket = input.getLastData().lastPacket;
                        if (inPacket != null)
                            update.append(inPacket.constructPacket());
                    }

                    for (String s : blocksToRemove)
                        update.append(s);

                    timePacket.setTime(stopwatch.getTime());
                    update.append(timePacket.constructPacket());

                    String res = update.toString();
                    for (MultiplayerHandle output : handles) {
                        output.send(res);
                    }
                }
                for (MultiplayerHandle handle : handles) {
                    handle.close();
                }
            }
        }).start();

        String colorUpdate = "";

        for (MultiplayerHandle input : handles) {
            MultiplayerPacket packet = new MultiplayerPacket();
            packet.setOption(MultiplayerPacket.COLOR);
            packet.setIndex(handles.indexOf(input));
            packet.setData(input.getLastData().color.toString());
            colorUpdate += packet.constructPacket();
        }

        for (MultiplayerHandle handle : handles) {
            handle.send(colorUpdate);
        }

        for (MultiplayerHandle handle : handles) {
            handle.send(START_PACKET);
        }
    }

    public void allowMove() {
        String packet = goPacket.constructPacket();
        for (MultiplayerHandle output : handles) {
            output.send(packet);
        }
    }

    public void stop() {
        this.continueRunning.set(false);
    }

    @Override
    public void render(Renderer renderer) {
        for (MultiplayerHandle handle : handles) {
            MultiplayerData data = handle.getLastData();
            if (data.lastPacket != null) {
                if (data.index != 0) {
                    int x = Math.round(data.lastPacket.getX() * Level.PPM) - Player.WIDTH / 2;
                    int y = Math.round(data.lastPacket.getY() * Level.PPM) - Player.HEIGHT / 2;
                    renderer.drawImage(x, y, Player.WIDTH, Player.HEIGHT, "player.png", data.color);
                }
            }
        }
    }

    public void setHostColor(Color color) {
        handles.get(0).getLastData().color = color;
    }

    public void removeBlock(int id) {
        MultiplayerPacket packet = new MultiplayerPacket();
        packet.setOption(MultiplayerPacket.REMOVE);
        packet.setBlockID(id);
        blocksToRemove.add(packet.constructPacket());
    }

    public interface ClientFoundCallback {
        void clientFound(MultiplayerHandle handle);
    }
}
