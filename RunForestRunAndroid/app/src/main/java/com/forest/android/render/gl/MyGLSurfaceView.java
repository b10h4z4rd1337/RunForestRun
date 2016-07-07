package com.forest.android.render.gl;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.forest.android.MainActivity;
import com.forest.android.input.AndroidInput;
import com.forest.android.music.AndroidMusicFactory;
import com.forest.android.net.bluetooth.AndroidBluetoothMultiplayerProvider;
import com.forest.android.net.lan.AndroidNetworkProvider;
import com.forest.input.Input;
import com.forest.input.InputFactory;
import com.forest.level.Level;
import com.forest.menu.MainMenu;
import com.forest.music.Music;
import com.forest.net.MultiplayerProvider;

/**
 * Created by Mathias on 28.05.16.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        Point p = getDisplaySize(context);

        renderer = new MyGLRenderer(context);
        renderer.setHeight(400);//size.y);
        renderer.setWidth((int) (400.0 * ((double)p.x / (double)p.y)));//size.x);

        Input.FACTORY = new InputFactory() {

            AndroidInput input;

            @Override
            public Input createInput() {
                if (input == null) {
                    input = new AndroidInput();
                    MyGLSurfaceView.this.setOnTouchListener(input);
                    MyGLSurfaceView.this.renderer.addRenderable(input);
                }
                return input;
            }
        };

        Music.MUSIC_FACTORY = new AndroidMusicFactory(context);

        if (BluetoothAdapter.getDefaultAdapter() != null) {
            MultiplayerProvider.availableProviders = new MultiplayerProvider[] { new AndroidNetworkProvider(), new AndroidBluetoothMultiplayerProvider() };
        } else {
            MultiplayerProvider.availableProviders = new MultiplayerProvider[] { new AndroidNetworkProvider() };
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    Point p = MyGLSurfaceView.getDisplaySize(MainActivity.CONTEXT);

                    double height = 400;
                    double width = (400.0 * ((double) p.x / (double) p.y));
                    renderer.touchDown((int) Math.round(event.getX() * (width / p.x)), 400 - (int) Math.round(event.getY() * (height / p.y)));
                    return true;
                }
                return false;
            }
        });

        renderer.addRenderable(new MainMenu(renderer));

        setRenderer(renderer);
    }

    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width, height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics size = new DisplayMetrics();
            display.getRealMetrics(size);
            width = size.widthPixels;
            height = size.heightPixels;
        } else {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }

        return new Point(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        renderer.setWidth(w);
        renderer.setHeight(h);
    }
}
