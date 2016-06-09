package com.forest.builder;

import com.forest.Rectangle;
import com.forest.builder.render.Grid;
import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.level.block.Block;
import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by Mathias on 07.06.16.
 */
public class BuilderLevel implements Renderable {

    private Rectangle spawnPoint, endPoint;
    private String backgroundImageName;

    private LinkedList<Block> blocksAfterScope = new LinkedList<>(), blocksBeforeScope = new LinkedList<>();
    private LinkedList<Block> blocksInScope = new LinkedList<>();

    private Grid grid = new Grid();

    public void addBlock(Block block) {
        this.blocksInScope.add(block);
        Collections.sort(this.blocksInScope, Level.BLOCK_COMPARATOR);
    }

    public void setStartPoint(int x) {
        spawnPoint = new Rectangle(x, 0, 5, 100);
    }

    public void setEndPoint(int x) {
        endPoint = new Rectangle(x, 0, 5, 100);
    }

    public void setBackgroundImageName(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
    }


    private int lastCamX = Integer.MIN_VALUE;

    private void updateBlocks(Rectangle camBounds) {
        if (lastCamX < camBounds.x) {
            while (!blocksInScope.isEmpty() && !blocksInScope.peekFirst().getBounds().intersects(camBounds)) {
                blocksBeforeScope.add(blocksInScope.removeFirst());
            }

            while (!blocksAfterScope.isEmpty() && blocksAfterScope.peekFirst().getBounds().intersects(camBounds)) {
                blocksInScope.add(blocksAfterScope.removeFirst());
            }
        } else if (lastCamX > camBounds.x){
            while (!blocksInScope.isEmpty() && !blocksInScope.peekLast().getBounds().intersects(camBounds)) {
                blocksAfterScope.add(0, blocksInScope.removeLast());
            }

            while (!blocksBeforeScope.isEmpty() && blocksBeforeScope.peekLast().getBounds().intersects(camBounds)) {
                blocksInScope.add(0, blocksBeforeScope.removeLast());
            }
        }
        lastCamX = camBounds.x;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.drawImagePrivate(0, 0, renderer.getWidth(), renderer.getHeight(), backgroundImageName);

        if (spawnPoint != null)
            renderer.drawRect(spawnPoint.x, spawnPoint.y, spawnPoint.width, renderer.getHeight(), Color.GREEN);

        if (endPoint != null)
            renderer.drawRect(endPoint.x, endPoint.y, endPoint.width, renderer.getHeight(), Color.BLUE);

        grid.render(renderer);

        for (Block block : blocksInScope) {
            block.render(renderer);
        }

        updateBlocks(renderer.getCamBounds());
    }

    public Block ray(int x, int y) {
        Rectangle tmp = new Rectangle(x, y, 1, 1);
        for (Block b : blocksInScope) {
            if (b.getBounds().intersects(tmp)) {
                return b;
            }
        }
        return null;
    }

    public LevelData toLevelData() {
        LevelData result = new LevelData();
        result.spawnPoint = spawnPoint;
        result.endPoint = endPoint;
        result.backgroundImage = backgroundImageName;

        LinkedList<Block> blocks = new LinkedList<>();
        blocks.addAll(blocksBeforeScope);
        blocks.addAll(blocksInScope);
        blocks.addAll(blocksAfterScope);
        Collections.sort(blocks, Level.BLOCK_COMPARATOR);
        result.blocks = blocks;

        return result;
    }

    public void loadFromLevelData(LevelData levelData) {
        this.backgroundImageName = levelData.backgroundImage;
        this.blocksAfterScope = levelData.blocks;

        this.spawnPoint = levelData.spawnPoint;
        this.endPoint = levelData.endPoint;

        this.lastCamX = Integer.MIN_VALUE;
    }

    public void resize() {
        LinkedList<Block> blocks = new LinkedList<>();
        blocks.addAll(blocksBeforeScope);
        blocks.addAll(blocksInScope);
        blocks.addAll(blocksAfterScope);
        Collections.sort(blocks, Level.BLOCK_COMPARATOR);

        blocksBeforeScope = new LinkedList<>();
        blocksInScope = new LinkedList<>();
        blocksAfterScope = blocks;
        this.lastCamX = Integer.MIN_VALUE;
    }
}
