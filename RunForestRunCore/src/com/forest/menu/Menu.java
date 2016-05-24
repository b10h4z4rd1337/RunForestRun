package com.forest.menu;

import com.forest.Rectangle;
import com.forest.level.Level;
import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.util.LinkedList;

/**
 * Created by Mathias on 23.05.16.
 */
public abstract class Menu implements Renderable {

    private LinkedList<Button> buttons = new LinkedList<>();
    private String backgroundImage = "";

    private Menu() { }

    public Menu(Renderer renderer) {

    }

    public Menu(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Button addButton(int x, int y, int width, int height, String text) {
        Button result = new Button(x, y, width, height, text);
        buttons.add(result);
        return result;
    }

    public void performClick(int x, int y) {
        Rectangle rectangle = new Rectangle(x, y, 1, 1);
        for (Button button : buttons) {
            if (button.getRectangle().intersects(rectangle)) {
                button.getPressedCallback().onClick();
            }
        }
    }

    @Override
    public void render(Renderer renderer) {
        renderer.drawImagePrivate(0, 0, renderer.getWidth(), renderer.getHeight(), backgroundImage);
        for (Button button : buttons) {
            button.render(renderer);
        }
    }
}
