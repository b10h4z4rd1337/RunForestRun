package com.forest.android.input;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.forest.Rectangle;
import com.forest.android.MainActivity;
import com.forest.android.music.AndroidMusic;
import com.forest.android.render.gl.MyGLRenderer;
import com.forest.android.render.gl.MyGLSurfaceView;
import com.forest.input.Input;
import com.forest.menu.Button;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.util.LinkedList;

/**
 * Created by Mathias on 23.05.16.
 */
public class AndroidInput extends Input implements View.OnTouchListener, Renderable {

    private double width, height, screenWidth, screenHeight;
    private ActionCallback upPressedActionCallback, downPressedActionCallback,
            leftPressedActionCallback, rightPressedActionCallback, jumpPressedActionCallback,
            upReleaseActionCallback, downReleaseActionCallback, leftReleaseActionCallback,
            rightReleaseActionCallback, jumpReleasedActionCallback;

    private LinkedList<Button> buttons = new LinkedList<>();

    public AndroidInput() {
        Point p = MyGLSurfaceView.getDisplaySize(MainActivity.CONTEXT);
        screenWidth = p.x;
        screenHeight = p.y;
        height = 400;
        width = (int) (400.0 * ((double)screenWidth / (double)screenHeight));

        Button leftButton = new Button(0, 0, 50, 50, "");
        leftButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.leftPressedActionCallback.execute();
            }
        });
        leftButton.setReleasedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.leftReleaseActionCallback.execute();
            }
        });
        buttons.add(leftButton);

        Button rightButton = new Button(120, 0, 50, 50, "");
        rightButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.rightPressedActionCallback.execute();
            }
        });
        rightButton.setReleasedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.rightReleaseActionCallback.execute();
            }
        });
        buttons.add(rightButton);

        Button upButton = new Button(60, 60, 50, 50, "");
        upButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.upPressedActionCallback.execute();
            }
        });
        upButton.setReleasedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.upReleaseActionCallback.execute();
            }
        });
        buttons.add(upButton);

        Button downButton = new Button(60, 0, 50, 50, "");
        downButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.downPressedActionCallback.execute();
            }
        });
        downButton.setReleasedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.downReleaseActionCallback.execute();
            }
        });
        buttons.add(downButton);

        Button jumpButton = new Button((int) (width - 50), 0, 50, 50, "");
        jumpButton.setPressedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.jumpPressedActionCallback.execute();
            }
        });
        jumpButton.setReleasedCallback(new Button.Callback() {
            @Override
            public void onClick() {
                AndroidInput.this.jumpReleasedActionCallback.execute();
            }
        });
        buttons.add(jumpButton);
    }

    @Override
    public void setPressedUpListener(ActionCallback actionCallback) {
        this.upPressedActionCallback = actionCallback;
    }

    @Override
    public void setPressedDownListener(ActionCallback actionCallback) {
        this.downPressedActionCallback = actionCallback;
    }

    @Override
    public void setPressedLeftListener(ActionCallback actionCallback) {
        this.leftPressedActionCallback = actionCallback;
    }

    @Override
    public void setPressedRightListener(ActionCallback actionCallback) {
        this.rightPressedActionCallback = actionCallback;
    }



    @Override
    public void setReleaseUpListener(ActionCallback actionCallback) {
        this.upReleaseActionCallback = actionCallback;
    }

    @Override
    public void setReleaseDownListener(ActionCallback actionCallback) {
        this.downReleaseActionCallback = actionCallback;
    }

    @Override
    public void setReleaseLeftListener(ActionCallback actionCallback) {
        this.leftReleaseActionCallback = actionCallback;
    }

    @Override
    public void setReleaseRightListener(ActionCallback actionCallback) {
        this.rightReleaseActionCallback = actionCallback;
    }



    @Override
    public void setPressedJumpListener(ActionCallback actionCallback) {
        this.jumpPressedActionCallback = actionCallback;
    }

    @Override
    public void setReleasedJumpListener(ActionCallback actionCallback) {
        this.jumpReleasedActionCallback = actionCallback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index = event.getActionIndex();
        System.out.println(index);
        Rectangle tmp = new Rectangle((int)Math.round(event.getX(index) * (width / screenWidth)), (int)height - (int)Math.round(event.getY(index) * (height / screenHeight)), 1, 1);
        for (Button button : buttons) {
            if (button.getRectangle().intersects(tmp)) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        button.getPressedCallback().onClick();
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        button.getReleasedCallback().onClick();
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(Renderer renderer) {
        for (Button button : buttons) {
            button.render(renderer);
        }
    }
}
