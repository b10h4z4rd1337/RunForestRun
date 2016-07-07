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
        int width = renderer.getWidth();
        int height = renderer.getHeight();

        for (int x = -1; x < width; x += Block.BLOCK_SIDE) {
            renderer.drawRect(x, 0, 2, height, Color.BLACK);
        }
    }

    private void renderHorizontalLines(Renderer renderer) {
        int width = renderer.getWidth();
        int height = renderer.getHeight();

        for (int y = -1 ; y < height + 1; y += Block.BLOCK_SIDE) {
            renderer.drawRect(0, y, width, 2, Color.BLACK);
        }
    }

}
