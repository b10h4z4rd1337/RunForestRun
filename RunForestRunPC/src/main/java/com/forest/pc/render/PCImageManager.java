package com.forest.pc.render;

import com.forest.render.Color;
import com.forest.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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

    private BufferedImage loadImage(String name) throws IOException {
            return ImageIO.read(Renderer.class.getResource(name));
    }

    Image getImage(String name) throws IOException {
        if (name == null)
            return null;
        if (name.isEmpty())
            return null;
        if (map.containsKey(name))
            return map.get(name);

        BufferedImage loadedImage = loadImage(name);
        map.put(name, loadedImage);
        return loadedImage;
    }

    public Image coloredImage(String name, Color color) throws IOException {
        if (name == null)
            return null;
        if (name.isEmpty())
            return null;

        String id = name + "_(" + color.r + "/" + color.g + "/" + color.b + "/" + color.a + ")";

        if (map.containsKey(id))
            return map.get(id);

        BufferedImage loadedImage = loadImage(name);
        WritableRaster raster = loadedImage.getRaster();

        int[] buffer = new int[4];
        for (int x = 0; x < loadedImage.getWidth(); x++)
            for (int y = 0; y < loadedImage.getHeight(); y++) {
                raster.getPixel(x, y, buffer);
                if (buffer[0] == 255 && buffer[1] == 255 && buffer[2] == 255 && buffer[3] == 255) {
                    buffer[0] = color.r;
                    buffer[1] = color.g;
                    buffer[2] = color.b;
                    buffer[3] = color.a;
                    raster.setPixel(x, y, buffer);
                }
            }

        //loadedImage = new BufferedImage(ColorModel.getRGBdefault(), raster, false, null);
        map.put(id, loadedImage);
        return loadedImage;
    }
}
