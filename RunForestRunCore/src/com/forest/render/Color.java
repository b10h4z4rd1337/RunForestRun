package com.forest.render;

/**
 * Created by Mathias on 23.05.16.
 */
public class Color {

    public int r, g, b, a;

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
