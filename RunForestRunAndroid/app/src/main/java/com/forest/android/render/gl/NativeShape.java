package com.forest.android.render.gl;

import android.opengl.GLES20;
import com.forest.render.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Mathias on 28.05.16.
 */
public class NativeShape {

    public static final String vertexShader =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    public static final String fragmentShader =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // Geometric variables
    public float[] vertices;
    public float[] color;
    public short[] indices = new short[] {0, 1, 2, 0, 2, 3};

    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;

    private int mProgram;

    public NativeShape() {
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2); // 2 bytes per short
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);


        int vertexShaderID = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderID = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShaderID);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShaderID);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void setBounds(float left, float top, float right, float bottom) {
        vertices = new float[] {
                left, top, 0.f,
                left, bottom, 0.f,
                right, bottom, 0.f,
                right, top, 0.f
        };

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4); // 4 bytes per float

        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void setColor(Color color) {
        this.color = new float[] {color.r / 255.f, color.g / 255.f, color.b / 255.f, color.a / 255.f};
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Get handle to texture coordinates location
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }
}
