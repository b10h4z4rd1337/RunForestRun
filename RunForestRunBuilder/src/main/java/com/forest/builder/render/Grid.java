package com.forest.builder.render;

import com.forest.level.block.Block;
import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 07.06.16.
 */
public class Grid implements Renderable {

    @Override
    public void render(Renderer renderer) {
        renderVerticalLines(renderer);
        renderHorizontalLines(renderer);
    }

    private void renderVerticalLines(Renderer renderer) {
        int startX = renderer.getCamBounds().x;
        startX = startX - (startX % Block.BLOCK_SIDE);
        int width = renderer.getWidth();
        int height = renderer.getHeight();

        for (int x = startX; x < startX + width; x += Block.BLOCK_SIDE) {
            renderer.drawRect(x - 1, 0, 2, height, Color.BLACK);
        }
    }

    private void renderHorizontalLines(Renderer renderer) {
        int width = renderer.getWidth();
        int height = renderer.getHeight();

        for (int y = 0; y < height; y += Block.BLOCK_SIDE) {
            renderer.drawRect(0, y - 1, width, 2, Color.BLACK);
        }
    }

}
