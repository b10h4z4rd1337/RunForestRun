package com.forest.android.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.forest.render.Renderer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mathias on 14.05.16.
 */
public class AndroidImageManager {

    private HashMap<String, Bitmap> map;
    private Context context;

    AndroidImageManager(Context context) {
        this.map = new HashMap<>();
        this.context = context;
    }

    Bitmap getImage(String name) throws IOException {
        if (map.containsKey(name))
            return map.get(name);
        else {
            Bitmap loadedImage = BitmapFactory.decodeStream(context.getAssets().open(name));
            map.put(name, loadedImage);
            return loadedImage;
        }
    }

}
