package com.forest.level;

import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 23.05.16.
 */
public class Stopwatch implements Renderable {

    private long startTime = 0;
    private int x = 0, y = 0;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public long getTime() {
        if (startTime != 0)
            return System.currentTimeMillis() - startTime;
        else
            return 0L;
    }

    public String getTimeText() {
        long time = getTime();
        return ((time - (time % 1000)) / 1000) + "." + (time % 1000);
    }

    public void setWidth(int width) {
        x = width;
    }

    public void setHeight(int height) {
        y = height;
    }

    @Override
    public void render(Renderer renderer) {
        setWidth(renderer.getWidth());
        setHeight(renderer.getHeight());
        String text = getTimeText();
        renderer.setTextSize(20);
        renderer.drawString(x - renderer.getTextWidth(text), y - renderer.getTextHeight(), text, new Color(255, 255, 255));
    }
}
