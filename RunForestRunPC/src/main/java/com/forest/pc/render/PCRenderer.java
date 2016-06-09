package com.forest.pc.render;

import com.forest.input.Input;
import com.forest.render.Color;
import com.forest.render.Renderer;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Mathias on 04.05.16.
 */
public class PCRenderer extends Renderer {

    private PCImageManager pcImageManager;
    private Graphics graphics;

    public PCRenderer(int width, int height, Input input) {
        super(width, height, input);
        pcImageManager = new PCImageManager();
    }

    public void setGraphics(Graphics g) {
        this.graphics = g;
    }

    @Override
    public void drawImagePrivate(float x, float y, float width, float height, String name) {
        y = this.getHeight() - y - height;
        Image image = null;
        try {
            image = pcImageManager.getImage(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            graphics.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
        }
    }

    @Override
    public void drawRect(int x, int y, int width, int height, Color color) {
        y = this.camera.height - y - height;
        x -= camera.x;
        graphics.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        graphics.fillRect(x, y, width, height);
    }

    @Override
    public void drawString(int x, int y, String text, Color color) {
        y = this.camera.height - y;
        graphics.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        graphics.drawString(text, x, y);
    }

    @Override
    public void setTextSize(int size) {
        graphics.setFont(graphics.getFont().deriveFont(size * 1.4f));
    }

    @Override
    public int getTextHeight() {
        return graphics.getFontMetrics().getHeight();
    }

    @Override
    public int getTextWidth(String text) {
        return graphics.getFontMetrics().stringWidth(text);
    }

}
