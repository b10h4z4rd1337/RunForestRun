package com.forest.menu;

import com.forest.render.Color;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 24.05.16.
 */
public class GameFinishedOverlay extends Menu {

    private static final String FINISHED = "FINISHED!";

    public GameFinishedOverlay(Renderer renderer) {
        super(renderer);
    }

    @Override
    public void render(Renderer renderer) {
        super.render(renderer);
        renderer.setTextSize(30);
        renderer.drawString(renderer.getWidth() / 2 - renderer.getTextWidth(FINISHED) / 2,
                renderer.getHeight() / 2 - renderer.getTextHeight() / 2, FINISHED, Color.WHITE);
    }
}
