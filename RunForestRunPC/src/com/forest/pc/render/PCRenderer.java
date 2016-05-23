package com.forest.pc.render;

import com.forest.render.*;
import com.forest.render.Color;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Mathias on 04.05.16.
 */
public class PCRenderer extends Renderer {

    private PCImageManager pcImageManager;
    private Graphics graphics;

    public PCRenderer(int width, int height) {
        super(width, height);
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
        y = this.rectangle.height - y - height;
        x -= rectangle.x;
        graphics.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
        graphics.fillRect(x, y, width, height);
    }

}
