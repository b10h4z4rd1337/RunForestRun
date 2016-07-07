package com.forest.net;

import com.forest.level.Player;
import org.jbox2d.common.Vec2;

/**
 * Created by Mathias on 25.06.2016.
 */
public class MultiplayerLocalHandle implements MultiplayerHandle {

    private Player player;
    private MultiplayerData data;

    public MultiplayerLocalHandle(Player player) {
        this.player = player;
        this.data = new MultiplayerData();
        this.data.index = 0;
    }

    @Override
    public void setServer(MultiplayerServer server) {

    }

    @Override
    public MultiplayerData getLastData() {
        MultiplayerPacket multiplayerPacket = new MultiplayerPacket();
        multiplayerPacket.setIndex(0);

        if (player.getTime() == 0) {
            if (player.getBody() != null) {
                multiplayerPacket.setOption(MultiplayerPacket.POS);
                Vec2 pos = player.getBody().getPosition();
                multiplayerPacket.setPos(pos.x, pos.y);
            }

        } else {
            multiplayerPacket.setOption(MultiplayerPacket.END);
            multiplayerPacket.setTime(player.getTime());
        }

        data.lastPacket = multiplayerPacket;

        return data;
    }

    @Override
    public void send(String data) {
        String[] packets = data.split("\\|");

        for (String packet : packets) {
            if (!packet.isEmpty()) {
                MultiplayerPacket multiplayerPacket = new MultiplayerPacket(packet);

                if (multiplayerPacket.getOption().equals(MultiplayerPacket.REMOVE))
                    ((MultiplayerLevel) player.getLevel()).removeBlockUpdate(multiplayerPacket.getBlockID());
            }
        }
    }

    @Override
    public void close() {

    }

}
