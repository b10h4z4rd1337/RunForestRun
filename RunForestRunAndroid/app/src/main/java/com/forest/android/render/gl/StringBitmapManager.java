package com.forest.android.render.gl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.HashMap;

/**
 * Created by Mathias on 29.05.16.
 */
public class StringBitmapManager {

    private class SavedStringInfo {
        public String string;
        public float size;

        public SavedStringInfo(String string, float size) {
            this.string = string;
            this.size = size;
        }

        @Override
        public int hashCode() {
            return string.hashCode() + ((Float)size).hashCode();
        }
    }

    private Canvas canvas = new Canvas();
    private Paint paint = new Paint();
    private float fontSize = 10.f;
    private HashMap<SavedStringInfo, Integer> map = new HashMap<>();

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
        //Canvas canvas = new Canvas(result);
        canvas.setBitmap(result);
        result.eraseColor(0);
        paint.setColor(Color.WHITE);
        canvas.drawText(text, 0, height, paint);
        return result;
    }

    /*
     * TODO: Implement efficent String Bitmap creating, saving, coloring and reusing
     */

    public int getStringTexture(String text) throws Exception {
        if (text == null)
            throw new NullPointerException();

        Integer result = map.get(new SavedStringInfo(text, fontSize));
        if (result == null) {
            SavedStringInfo savedStringInfo = new SavedStringInfo(text, fontSize);
            Bitmap bitmap = createBitmapFromString(text);
            Integer id = MyGLRenderer.loadBitmapIntoGL(bitmap);
            bitmap.recycle();
            map.put(savedStringInfo, id);
            return id;
        } else {
            return result;
        }
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
