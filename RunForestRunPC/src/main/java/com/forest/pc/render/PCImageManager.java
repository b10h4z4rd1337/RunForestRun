package com.forest.pc.render;

import com.forest.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    Image getImage(String name) throws IOException {
        if (name == null)
            return null;
        if (name.isEmpty())
            return null;
        if (map.containsKey(name))
            return map.get(name);
        else {
            BufferedImage loadedImage = ImageIO.read(Renderer.class.getResource(name));
            map.put(name, loadedImage);
            return loadedImage;
        }
    }
}
