package com.forest.android.input;

import android.view.MotionEvent;
import android.view.View;

import com.forest.Rectangle;
import com.forest.input.Input;
import com.forest.menu.Button;
import com.forest.render.Renderable;
import com.forest.render.Renderer;

import java.util.LinkedList;

/**
 * Created by Mathias on 23.05.16.
 */
public class AndroidInput implements Input, View.OnTouchListener, Renderable {

    private int width, height;

    private ActionCallback upPressedActionCallback, downPressedActionCallback,
            leftPressedActionCallback, rightPressedActionCallback, jumpPressedActionCallback,
            upReleaseActionCallback, downReleaseActionCallback, leftReleaseActionCallback,
            rightReleaseActionCallback, jumpReleasedActionCallback;

    private LinkedList<Button> buttons = new LinkedList<>();

    public AndroidInput(int width, int height) {
        this.width = width;
        this.height = height;
        Button leftButton = new Button(0, 0, 100, 100, "");
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

        Button rightButton = new Button(220, 0, 100, 100, "");
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

        Button upButton = new Button(110, 110, 100, 100, "");
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

        Button downButton = new Button(110, 0, 100, 100, "");
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

        Button jumpButton = new Button(width - 100, 0, 100, 100, "");
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
        Rectangle tmp = new Rectangle(Math.round(event.getX()), Math.round(height - event.getY()), 1, 1);
        for (Button button : buttons) {
            if (button.getRectangle().intersects(tmp)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.getPressedCallback().onClick();
                        return true;
                    case MotionEvent.ACTION_UP:
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

    public void setWidth(int width) {
        this.width = width;
        Button jumpButton = buttons.peekLast();
        Rectangle rect = jumpButton.getRectangle();
        rect.x = width - rect.width;
        jumpButton.setRectangle(rect);
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
