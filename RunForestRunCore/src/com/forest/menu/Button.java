package com.forest.menu;

import com.forest.Rectangle;
import com.forest.render.*;
import com.forest.render.Color;

/**
 * Created by Mathias on 23.05.16.
 */
public class Button implements Renderable {

    private Rectangle rectangle;
    private Color color;
    private Callback pressedCallback, releasedCallback;
    private String text;

    public Button(int x, int y, int width, int height, String text) {
        rectangle = new Rectangle(x, y, width, height);
        this.text = text;
        this.pressedCallback = new Callback() {
            @Override
            public void onClick() {

            }
        };
        this.releasedCallback = new Callback() {
            @Override
            public void onClick() {

            }
        };
        this.color = new Color(192, 192, 192, 150);
    }

    public void setColor(com.forest.render.Color color) {
        this.color = color;
    }

    public void setPressedCallback(Callback callback) {
        this.pressedCallback = callback;
    }
    public Callback getPressedCallback() {
        return pressedCallback;
    }

    public void setReleasedCallback(Callback callback) {
        this.releasedCallback = callback;
    }
    public Callback getReleasedCallback() {
        return releasedCallback;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void render(Renderer renderer, long deltaTimeInMs) {
        renderer.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, color);
        renderer.drawString(rectangle.x + rectangle.width / 2 - renderer.getTextWidth(text) / 2, rectangle.y + rectangle.height / 2 - renderer.getTextHeight() / 2, text);
    }

    public interface Callback {
        void onClick();
    }
}
