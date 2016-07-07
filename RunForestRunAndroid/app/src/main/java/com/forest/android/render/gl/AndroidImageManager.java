package com.forest.android.render.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.forest.render.Color;

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

    public int getImage(String name) throws Exception {
        if (name == null)
            throw new NullPointerException();
        if (name.isEmpty())
            return -1;
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

    public int getColoredImage(String name, Color color) throws Exception {
        if (name == null)
            throw new NullPointerException();
        if (name.isEmpty())
            throw new Exception("name must not be empty");

        String id = name + "_(" + color.r + "/" + color.g + "/" + color.b + "/" + color.a + ")";
        if (map.containsKey(id))
            return map.get(id);

        Bitmap bmp = BitmapFactory.decodeStream(context.getAssets().open(name));
        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(android.graphics.Color.argb(color.a, color.r, color.g, color.b), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(bmp.copy(bmp.getConfig(), true));
        canvas.drawBitmap(bmp, 0, 0, paint);

        Integer loadedImage = MyGLRenderer.loadBitmapIntoGL(bmp);
        bmp.recycle();
        map.put(id, loadedImage);
        return loadedImage;
    }
}
