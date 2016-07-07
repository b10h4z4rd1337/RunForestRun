package com.forest.render;

import com.forest.Rectangle;
import com.forest.menu.Menu;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Mathias on 04.05.2016.
 */
public abstract class Renderer {

    private LinkedBlockingDeque<Renderable> thingsToRender;
    protected Rectangle camera;
    private long lastDrawTime = 0, deltaTime = 0;

    public Renderer(int width, int height) {
        camera = new Rectangle(0, 0, width, height);
        lastDrawTime = System.currentTimeMillis();

        thingsToRender = new LinkedBlockingDeque<>();
    }

    public void touchDown(int x, int y) {
        for (Renderable renderable : thingsToRender) {
            if (renderable instanceof Menu) {
                Menu menu = (Menu) renderable;
                menu.performClick(x, y);
            }
        }
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
        thingsToRender.addLast(renderable);
    }

    public void clear() {
        thingsToRender.clear();
    }

    public void drawImage(float x, float y, float width, float height, String name) {
        drawImage(x, y, width, height, name, null);
    }

    public void drawImage(float x, float y, float width, float height, String name, Color color) {
        drawImagePrivate((x - camera.x), (y - camera.y), width, height, name, color);
    }
    public void drawImagePrivate(float x, float y, float width, float height, String name) {
        drawImagePrivate(x, y, width, height, name, null);
    }
    public abstract void drawImagePrivate(float x, float y, float width, float height, String name, Color color);
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

    public long getDeltaTime() {
        return deltaTime;
    }

    private boolean update;

    public void needsUpdate() {
        this.update = true;
    }

    public boolean isUpdateNeeded() {
        if (this.update) {
            update = false;
            return true;
        }
        return false;
    }
}
