package com.forest.menu;

import com.forest.render.Color;
import com.forest.render.Renderer;

/**
 * Created by user on 06.06.2016.
 */
public class GameOverOverlay extends Menu {

    private static final String FINISHED = "Game Over!!";
    private String time;

    public GameOverOverlay(Renderer renderer, String time) {
        super(renderer);
        this.time = time;
    }

    @Override
    public void render(Renderer renderer) {
        super.render(renderer);
        renderer.setTextSize(30);
        int finishedHeight = renderer.getTextHeight();
        renderer.drawString(renderer.getWidth() / 2 - renderer.getTextWidth(FINISHED) / 2,
                renderer.getHeight() / 2 - renderer.getTextHeight() / 2, FINISHED, new Color(255, 0 ,0 ));
        String timeText = "You died after: " + time;
        renderer.setTextSize(20);
        renderer.drawString(renderer.getWidth() / 2 - renderer.getTextWidth(timeText) / 2,
                renderer.getHeight() / 2 - renderer.getTextHeight() / 2 - finishedHeight, timeText, new Color(255, 0 ,0 ));
    }
}