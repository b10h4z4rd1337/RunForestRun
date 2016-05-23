package com.forest.android.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.forest.render.Color;
import com.forest.render.Renderer;

import java.io.IOException;

/**
 * Created by Mathias on 14.05.16.
 */
public class AndroidRenderer extends Renderer {

    private AndroidImageManager imageManager;
    private Canvas canvas;
    private Paint paint = new Paint();

    public AndroidRenderer(int width, int height, Context context) {
        super(width, height);
        imageManager = new AndroidImageManager(context);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void drawImagePrivate(float x, float y, float width, float height, String name) {
        y = this.getHeight() - y - height;
        Bitmap bitmap = null;
        try {
            bitmap = imageManager.getImage(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int)width, (int)height, false);
            canvas.drawBitmap(bitmap, x, y, paint);
            paint.reset();
        }
    }

    @Override
    public void drawRect(int x, int y, int width, int height, Color color) {
        y = this.getHeight() - y - height;
        paint.setColor(android.graphics.Color.argb(color.a, color.r, color.g, color.b));
        canvas.drawRect(x, y, x + width, y + height, paint);
        paint.reset();
    }
}
