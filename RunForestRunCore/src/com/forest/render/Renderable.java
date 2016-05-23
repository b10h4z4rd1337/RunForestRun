package com.forest.render;

/**
 * Created by Mathias on 06.05.16.
 */
public interface Renderable {
    void render(Renderer renderer, long deltaTimeInMs);
}
