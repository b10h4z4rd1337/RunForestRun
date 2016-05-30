package com.forest.android.render.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.forest.android.input.AndroidInput;
import com.forest.level.Level;

/**
 * Created by Mathias on 28.05.16.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;
    private final AndroidInput androidInput;

    public MyGLSurfaceView(Context context) {
        super(context);

        androidInput = new AndroidInput(0, 0);

        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer(context, androidInput);

        Level level = Level.createTestLevel();
        renderer.addRenderable(level);
        renderer.addRenderable(androidInput);

        setRenderer(renderer);
        setOnTouchListener(androidInput);
    }


}
