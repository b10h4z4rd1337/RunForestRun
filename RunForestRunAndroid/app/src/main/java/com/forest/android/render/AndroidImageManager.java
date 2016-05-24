package com.forest.android.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.forest.render.Renderer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mathias on 14.05.16.
 */
public class AndroidImageManager {

    private LruCache<String, Bitmap> map;
    private Context context;

    AndroidImageManager(Context context) {
        this.map = new LruCache<>(10);
        this.context = context;
    }

    Bitmap getImage(String name) throws IOException {
        Bitmap result = map.get(name);
        if (result != null)
            return result;
        else {
            Bitmap loadedImage = BitmapFactory.decodeStream(context.getAssets().open(name));
            map.put(name, loadedImage);
            return loadedImage;
        }
    }

}
