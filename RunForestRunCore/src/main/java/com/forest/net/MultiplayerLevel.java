package com.forest.net;

import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.level.Player;
import com.forest.level.Stopwatch;
import com.forest.level.block.Block;
import com.forest.render.Renderer;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mathias on 26.06.2016.
 */
public class MultiplayerLevel extends Level {

    //Game Dynamics
    private MultiplayerProvider provider;
    private MultiplayerServer server;
    private int index;
    protected volatile ConcurrentLinkedQueue<Integer> blocksToRemove = new ConcurrentLinkedQueue<>();

    public MultiplayerLevel(LevelData levelData, Stopwatch stopwatch, MultiplayerProvider provider, MultiplayerServer server, int index) {
        super(levelData);
        this.provider = provider;
        this.stopwatch = stopwatch;
        this.server = server;
        this.index = index;
    }

    @Override
    public void receivedEndPoint(Player player) {
        MultiplayerPacket packet = new MultiplayerPacket();
        packet.setIndex(index);
        packet.setOption(MultiplayerPacket.END);
        packet.setTime(player.getTime());
        provider.send(packet.constructPacket());
    }

    @Override
    public void removeBlock(Block block) {
        super.removeBlock(block);
        if (server == null) {
            MultiplayerPacket packet = new MultiplayerPacket();
            packet.setOption(MultiplayerPacket.REMOVE);
            packet.setBlockID(block.getID());
            provider.send(packet.constructPacket());
        } else {
            server.removeBlock(block.getID());
        }
    }

    void removeBlockUpdate(int id) {
        blocksToRemove.add(id);
    }

    @Override
    protected void removeBlocksUpdate() {
        Block foundBlock = null;

        for (Integer id : blocksToRemove) {

            for (Iterator<Block> iter = blocksInScope.iterator(); iter.hasNext(); ) {
                Block block = iter.next();
                if (block != null) {
                    if (block.getID() == id) {
                        foundBlock = block;
                        iter.remove();
                        break;
                    }
                }
            }

            for (Iterator<Block> iter = blocksBeforeScope.iterator(); iter.hasNext(); ) {
                Block block = iter.next();
                if (block != null) {
                    if (block.getID() == id) {
                        foundBlock = block;
                        iter.remove();
                        break;
                    }
                }
            }

            for (Iterator<Block> iter = blocksAfterScope.iterator(); iter.hasNext(); ) {
                Block block = iter.next();
                if (block != null) {
                    if (block.getID() == id) {
                        foundBlock = block;
                        iter.remove();
                        break;
                    }
                }
            }

            if (foundBlock != null) {
                bodiesToRemove.add(foundBlock.getBody());
                foundBlock = null;
            }
        }
    }
}
