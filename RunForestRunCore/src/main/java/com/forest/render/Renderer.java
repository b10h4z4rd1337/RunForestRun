package com.forest.render;

import com.forest.Rectangle;
import com.forest.input.Input;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mathias on 04.05.2016.
 */
public abstract class Renderer {

    private Input input;
    private ConcurrentLinkedQueue<Renderable> thingsToRender;
    protected Rectangle camera;
    private long lastDrawTime = 0, deltaTime = 0;

    public Renderer(int width, int height, Input input) {
        this.input = input;
        camera = new Rectangle(0, 0, width, height);
        lastDrawTime = System.currentTimeMillis();

        thingsToRender = new ConcurrentLinkedQueue<>();
    }

    public void render() {
        long timeNow = System.currentTimeMillis();
        deltaTime = timeNow - lastDrawTime;
        lastDrawTime = timeNow;

        for (Renderable renderable : thingsToRender) {
            renderable.render(this);
        }
/*
        long timeToRender = System.currentTimeMillis() - timeNow;
        if (timeToRender < 1000.0 / 60.0) {
            try {
                Thread.sleep((long) (1000.0 / 60.0 - timeToRender));
            } catch (Exception ignored) { }
        }*/
    }

    public void setCamPos(int x, int y) {
        camera.x = x;
        camera.y = y;
    }

    public void addRenderable(Renderable renderable) {
        thingsToRender.add(renderable);
    }

    public void clear() {
        thingsToRender.clear();
    }

    public void drawImage(float x, float y, float width, float height, String name) {
        drawImagePrivate((x - camera.x), y, width, height, name);
    }
    public abstract void drawImagePrivate(float x, float y, float width, float height, String name);
    public abstract void drawRect(int x, int y, int width, int height, Color color);
    public void drawString(int x, int y, String text) { drawString(x, y, text, new Color(0, 0, 0)); }
    public abstract void drawString(int x, int y, String text, Color color);
    public abstract void setTextSize(int size);
    public abstract int getTextHeight();
    public abstract int getTextWidth(String text);

    public int getWidth() {
        return camera.width;
    }

    public void setWidth(int width) {
        this.camera.width = width;
    }

    public int getHeight() {
        return camera.height;
    }

    public void setHeight(int height) {
        this.camera.height = height;
    }

    public Rectangle getCamBounds() {
        return camera;
    }

    public Input getInput() {
        return input;
    }

    public long getDeltaTime() {
        return deltaTime;
    }
}
