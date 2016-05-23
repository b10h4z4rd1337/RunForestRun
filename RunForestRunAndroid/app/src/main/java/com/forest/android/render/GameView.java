package com.forest.android.render;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.forest.level.Level;

/**
 * Created by Mathias on 14.05.16.
 */
public class GameView extends View {

    private AndroidRenderer androidRenderer;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        androidRenderer = new AndroidRenderer(this.getWidth(), this.getHeight(), getContext());

        Level level = new Level("background_dark.png");
        for (int i = -100; i < 1000; i += 50) {
            level.createGroundBlock(i, 0, 50, 50, "block.png");
        }
        level.createGroundBlock(100, 50, 50, 50, "block.png");
        level.createGroundBlock(150, 100, 50, 50, "block.png");
        level.createGroundBlock(200, 150, 50, 50, "block.png");
        level.createGroundBlock(0, 0, 50, 50, "block.png");
        //level.createPlayer(0, 50, 50, 80, "player.png", pcInput);

        androidRenderer.addRenderable(level);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        androidRenderer.setWidth(w);
        androidRenderer.setHeight(h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        androidRenderer.setCanvas(canvas);
        androidRenderer.render();
        invalidate();
    }
}
