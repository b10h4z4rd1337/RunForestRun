package com.forest.android.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mathias on 14.05.16.
 */
public class AndroidImageManager {

    private HashMap<String, Bitmap> map;
    private Context context;

    public AndroidImageManager(Context context) {
        this.map = new HashMap<>();
        this.context = context;
    }

    public Bitmap getImage(String name) throws IOException {
        if (name == null)
            return null;
        if (name.isEmpty())
            return null;
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
