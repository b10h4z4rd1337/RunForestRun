package com.forest.menu;

import com.forest.render.Color;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 30.06.16.
 */
public class ColorPicker extends Menu {

    public static final int SIDE = 50, GAP = 10;
    private static final Color[] colors = {
            new Color(255, 0, 0), //RED
            new Color(255, 69, 0), //ORANGE
            new Color(255, 255, 0), //YELLOW
            new Color(0, 255, 0), //GREEN
            new Color(0, 0, 255), //BLUE
            new Color(0, 0, 0) //BLACK
    };

    private int x, y, width, height;

    public ColorPicker(int x, int y, Renderer renderer, final ColorPickedCallback callback) {
        super("");

        y = y + SIDE * 2 + GAP;

        for (int h = 0; h < 2; h++) {
            for (int i = 0; i < 3; i++) {
                final Color color = colors[i + colors.length / 2 * h];
                Button button = addButton(x + SIDE * i + GAP * i, y - (SIDE * (h + 1)) - GAP * h, SIDE, SIDE, " ");
                button.setColor(color);
                button.setPressedCallback(new Button.Callback() {
                    @Override
                    public void onClick() {
                        callback.colorPicked(color);
                    }
                });
            }
        }
    }

    @Override
    public void render(Renderer renderer) {
        for (Button button : getButtons())
            button.render(renderer);
    }

    public interface ColorPickedCallback {
        void colorPicked(Color color);
    }
}
