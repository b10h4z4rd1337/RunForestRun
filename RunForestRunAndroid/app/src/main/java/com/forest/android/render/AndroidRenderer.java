package com.forest.android.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.forest.input.Input;
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

    public AndroidRenderer(int width, int height, Input input, Context context) {
        super(width, height, input);
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

    @Override
    public void drawString(int x, int y, String text, Color color) {
        paint.setColor(android.graphics.Color.argb(color.a, color.r, color.g, color.b));
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void setTextSize(int size) {
        paint.setTextSize(size * 1.4f);
    }

    @Override
    public int getTextHeight() {
        Rect rect = new Rect();
        paint.getTextBounds("I", 0, 1, rect);
        return rect.height();
    }

    @Override
    public int getTextWidth(String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }
}
