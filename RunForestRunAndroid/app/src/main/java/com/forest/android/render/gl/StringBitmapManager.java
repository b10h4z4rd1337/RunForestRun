package com.forest.android.render.gl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Mathias on 29.05.16.
 */
public class StringBitmapManager {

    private Canvas canvas = new Canvas();
    private Paint paint = new Paint();
    private float fontSize = 10.f;

    private Bitmap createBitmapFromString(String text) {
        int width, height;

        if (!text.isEmpty()) {
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect);
            width = rect.width();
            height = rect.height();
        } else {
            width = 1;
            height = 1;
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        canvas.setBitmap(result);
        result.eraseColor(0);
        paint.setColor(Color.WHITE);
        canvas.drawText(text, 0, height, paint);
        return result;
    }

    /*
     * TODO: Implement efficent String Bitmap coloring and reusing
     */

    public int getStringTexture(String text) throws Exception {
        if (text == null)
            throw new NullPointerException();

        Bitmap bitmap = createBitmapFromString(text);
        Integer id = MyGLRenderer.loadBitmapIntoGL(bitmap);
        bitmap.recycle();
        return id;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        paint.setTextSize(fontSize * 1.4f);
    }

    public int getTextHeight() {
        Rect rect = new Rect();
        paint.getTextBounds("I", 0, 1, rect);
        return rect.height();
    }

    public int getTextWidth(String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

}
