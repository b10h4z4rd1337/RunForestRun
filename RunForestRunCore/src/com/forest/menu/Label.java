package com.forest.menu;

import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 24.05.16.
 */
public class Label implements Renderable {

    private String text;
    private int x, y;

    public Label(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.drawString(x, y, text, new Color(255, 255, 255));
    }
}
