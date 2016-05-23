package com.forest.menu;

import com.forest.render.*;
import com.forest.render.Color;

import java.awt.*;

/**
 * Created by Mathias on 23.05.16.
 */
public class Button implements Renderable {

    private Rectangle rectangle;
    private com.forest.render.Color color;
    private Callback callback;
    private String text;

    public Button(int x, int y, int width, int height, String text) {
        rectangle = new Rectangle(x, y, width, height);
        this.text = text;
        this.callback = () -> { };
        java.awt.Color grey = java.awt.Color.LIGHT_GRAY;
        this.color = new Color(grey.getRed(), grey.getGreen(), grey.getBlue(), grey.getAlpha());
    }

    public void setColor(com.forest.render.Color color) {
        this.color = color;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void render(Renderer renderer, long deltaTimeInMs) {
        renderer.drawRect(0, 0, 100, 50, color);
    }

    @FunctionalInterface
    public interface Callback {
        void onClick();
    }
}
