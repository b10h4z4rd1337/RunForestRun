package com.forest.net;

import com.forest.input.Input;
import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.level.Player;
import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MultiplayerClient implements Renderable {

    private final AtomicReference<ArrayList<MultiplayerData>> lastPackets = new AtomicReference<>();
    private int index = -1;
    private LevelData levelData;
    private long time = 0L;
    private Player player;
    private Color color;
    private MultiplayerProvider provider;

    public MultiplayerClient(final MultiplayerProvider provider, final ClientReadyCallback callback) {
        this.provider = provider;
        lastPackets.set(new ArrayList<MultiplayerData>());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String data = "";
                    while (!data.endsWith("|"))
                        data += provider.receive();
                    String[] packets = data.split("\\|");

                    for (String packet : packets) {
                        if (!packet.isEmpty()) {
                            MultiplayerPacket multiplayerPacket = new MultiplayerPacket(packet);

                            if (multiplayerPacket.getOption().equals(MultiplayerPacket.INDEX)) {
                                index = multiplayerPacket.getIndex();
                                if (color != null)
                                    sendColor(color);
                            } else if (multiplayerPacket.getOption().equals(MultiplayerPacket.LEVEL)) {
                                try {
                                    levelData = MultiplayerUtils.stringToLevelData(multiplayerPacket.getData());
                                } catch (IOException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else if (multiplayerPacket.getOption().equals(MultiplayerPacket.START)) {
                                callback.clientReady(MultiplayerClient.this);
                                player.setColor(color);
                            } else if (multiplayerPacket.getOption().equals(MultiplayerPacket.TIME)) {
                                time = multiplayerPacket.getTime();
                            } else if (multiplayerPacket.getOption().equals(MultiplayerPacket.GO)) {
                                player.setupInput(Input.FACTORY.createInput());
                            } else if (multiplayerPacket.getOption().equals(MultiplayerPacket.REMOVE)) {
                                ((MultiplayerLevel) player.getLevel()).removeBlockUpdate(multiplayerPacket.getBlockID());
                            } else {
                                ArrayList<MultiplayerData> packetList = lastPackets.get();
                                if (packetList.size() < multiplayerPacket.getIndex() + 1) {
                                    for (int i = 0; i < multiplayerPacket.getIndex() + 1 - packetList.size(); i++)
                                        packetList.add(null);
                                }
                                MultiplayerData mpData = packetList.get(multiplayerPacket.getIndex());
                                if (mpData == null)
                                    mpData = new MultiplayerData();
                                if (multiplayerPacket.getOption().equals(MultiplayerPacket.COLOR))
                                    mpData.color = Color.fromString(multiplayerPacket.getData());
                                mpData.index = multiplayerPacket.getIndex();
                                mpData.lastPacket = multiplayerPacket;
                                packetList.set(multiplayerPacket.getIndex(), mpData);
                                lastPackets.set(packetList);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public long getTime() {
        return time;
    }

    public LevelData getLevelData() {
        return levelData;
    }

    public int getIndex() {
        return index;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void render(Renderer renderer) {
        for (MultiplayerData packet : lastPackets.get()) {
            if (packet != null) {
                if (packet.lastPacket != null) {
                    if (index != packet.lastPacket.getIndex()) {
                        int x = Math.round(packet.lastPacket.getX() * Level.PPM) - Player.WIDTH / 2;
                        int y = Math.round(packet.lastPacket.getY() * Level.PPM) - Player.HEIGHT / 2;
                        renderer.drawImage(x, y, Player.WIDTH, Player.HEIGHT, "player.png", packet.color);
                    }
                }
            }
        }
    }

    public void setColor(Color color) {
        if (index != -1)
            sendColor(color);
        else
            this.color = color;

    }

    private void sendColor(Color color) {
        MultiplayerPacket packet = new MultiplayerPacket();
        packet.setOption(MultiplayerPacket.COLOR);
        packet.setIndex(index);
        packet.setData(this.color.toString());
        provider.send(packet.constructPacket());
    }

    public interface ClientReadyCallback {
        void clientReady(MultiplayerClient client);
    }
}
