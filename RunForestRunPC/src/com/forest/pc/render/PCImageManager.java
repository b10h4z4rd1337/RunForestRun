package com.forest.pc.render;

import com.forest.render.Renderer;
import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mathias on 04.05.16.
 */
public class PCImageManager {

    private HashMap<String, Image> map;

    PCImageManager() {
        map = new HashMap<>();
    }

    Image getImage(@NotNull String name) throws IOException {
        if (map.containsKey(name))
            return map.get(name);
        else {
            Image loadedImage = ImageIO.read(Renderer.class.getResource(name));
            map.put(name, loadedImage);
            return loadedImage;
        }
    }
}
