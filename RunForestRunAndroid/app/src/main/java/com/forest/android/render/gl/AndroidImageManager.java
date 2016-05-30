package com.forest.android.render.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

/**
 * Created by Mathias on 14.05.16.
 */
public class AndroidImageManager {

    private HashMap<String, Integer> map;
    private Context context;

    public AndroidImageManager(Context context) {
        this.map = new HashMap<>();
        this.context = context;
    }

    public Integer getImage(String name) throws Exception {
        if (name == null)
            throw new NullPointerException();
        if (name.isEmpty())
            throw new Exception("name must not be empty");
        Integer result = map.get(name);
        if (result != null)
            return result;
        else {
            Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open(name));
            Integer loadedImage = MyGLRenderer.loadBitmapIntoGL(bmp);
            bmp.recycle();
            map.put(name, loadedImage);
            return loadedImage;
        }
    }

}
