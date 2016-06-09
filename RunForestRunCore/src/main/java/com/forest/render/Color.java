package com.forest.render;

/**
 * Created by Mathias on 23.05.16.
 */
public class Color {

    public int r, g, b, a;
    public static final Color BLACK = new Color(0, 0, 0), WHITE = new Color(255, 255, 255),
                        GREEN = new Color(0, 255, 0), BLUE = new Color(0, 0, 255);

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

}
