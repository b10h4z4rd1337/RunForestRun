package com.forest.render;

import com.forest.Rectangle;

import java.util.LinkedList;

/**
 * Created by Mathias on 04.05.2016.
 */
public abstract class Renderer {

    private LinkedList<Renderable> thingsToRender;
    protected Rectangle rectangle;
    private long lastDrawTime = 0;

    public Renderer(int width, int height) {
        rectangle = new Rectangle(0, 0, width, height);
        lastDrawTime = System.currentTimeMillis();

        thingsToRender = new LinkedList<>();
    }

    public void render() {
        long timeNow = System.currentTimeMillis();
        long deltaTime = timeNow - lastDrawTime;
        lastDrawTime = timeNow;

        for (Renderable renderable : thingsToRender) {
            renderable.render(this, deltaTime);
        }

        long timeToRender = System.currentTimeMillis() - timeNow;
        if (timeToRender < 1000.0 / 60.0) {
            try {
                Thread.sleep((long) (1000.0 / 60.0 - timeToRender));
            } catch (Exception ignored) { }
        }
    }

    public void setCamPos(int x, int y) {
        rectangle.x = x;
        rectangle.y = y;
    }

    public void addRenderable(Renderable renderable) {
        thingsToRender.add(renderable);
    }

    public void drawImage(float x, float y, float width, float height, String name) {
        drawImagePrivate((x - rectangle.x), y, width, height, name);
    }
    public abstract void drawImagePrivate(float x, float y, float width, float height, String name);
    public abstract void drawRect(int x, int y, int width, int height, Color color);

    public int getWidth() {
        return rectangle.width;
    }

    public void setWidth(int width) {
        this.rectangle.width = width;
    }

    public int getHeight() {
        return rectangle.height;
    }

    public void setHeight(int height) {
        this.rectangle.height = height;
    }

    public Rectangle getCamBounds() {
        return rectangle;
    }
}
