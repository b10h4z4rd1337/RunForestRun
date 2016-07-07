package com.forest.android.render.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import com.forest.android.input.AndroidInput;
import com.forest.render.Color;
import com.forest.render.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;

/**
 * Created by Mathias on 28.05.16.
 */
public class MyGLRenderer extends Renderer implements GLSurfaceView.Renderer {

    private AndroidImageManager imageManager;
    private StringBitmapManager stringBitmapManager;

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    private Shape shape;
    private NativeShape nativeShape;

    public MyGLRenderer(Context context) {
        super(0, 0);
        imageManager = new AndroidImageManager(context);
        stringBitmapManager = new StringBitmapManager();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.f, 0.f, 0.f, 1.f);
        GLES20.glEnable(GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        shape = new Shape();
        nativeShape = new NativeShape();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int screen_width, int screen_height) {

        int vHeight = 400, vWidth = (int) ((float) vHeight * ((float) screen_width / (float) screen_height));

        GLES20.glViewport(0,0,screen_width,screen_height);

        setWidth(vWidth);
        setHeight(vHeight);

        // Clear matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0, vWidth, 0, vHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        render();
    }

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static int loadBitmapIntoGL(Bitmap bmp) throws IOException {
        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        return texturenames[0];
    }

    @Override
    public void drawImagePrivate(float x, float y, float width, float height, String name, Color color) {
        try {
            if (color == null) {
                int id = imageManager.getImage(name);
                if (id != -1)
                    shape.setTextureID(id);
                else
                    return;
            } else {
                shape.setTextureID(imageManager.getColoredImage(name, color));
            }
            shape.setBounds(x, y + height, x + width, y);
            shape.draw(mtrxProjectionAndView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawRect(int x, int y, int width, int height, Color color) {
        nativeShape.setColor(color);
        nativeShape.setBounds(x, y + height, x + width, y);
        nativeShape.draw(mtrxProjectionAndView);
    }

    @Override
    public void drawString(int x, int y, String text, Color color) {
        try {
            shape.setTextureID(stringBitmapManager.getStringTexture(text));
            shape.setBounds(x, y + stringBitmapManager.getTextHeight(), x + stringBitmapManager.getTextWidth(text), y);
            shape.draw(mtrxProjectionAndView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTextSize(int size) {
        stringBitmapManager.setFontSize(size);
    }

    @Override
    public int getTextHeight() {
        return stringBitmapManager.getTextHeight();
    }

    @Override
    public int getTextWidth(String text) {
        return stringBitmapManager.getTextWidth(text);
    }
}
