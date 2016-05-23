package com.forest.menu;

import com.forest.Rectangle;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.util.LinkedList;

/**
 * Created by Mathias on 23.05.16.
 */
public class Menu implements Renderable {

    private LinkedList<Button> buttons;

    public Menu() {
        buttons = new LinkedList<>();
        buttons.add(new Button(0, 0, 100, 50, "Hi"));
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
    public void render(Renderer renderer, long deltaTimeInMs) {
        for (Button button : buttons) {
            button.render(renderer, deltaTimeInMs);
        }
    }
}
