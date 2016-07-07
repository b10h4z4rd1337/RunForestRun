package com.forest.pc.input;

import com.forest.input.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Mathias on 09.05.16.
 */
public class PCInput extends Input implements KeyListener {

    private ActionCallback upPressedActionCallback, downPressedActionCallback,
            leftPressedActionCallback, rightPressedActionCallback, jumpPressedActionCallback,
            upReleaseActionCallback, downReleaseActionCallback, leftReleaseActionCallback,
            rightReleaseActionCallback, jumpReleasedActionCallback;

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
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressedActionCallback.execute();
                break;
            case KeyEvent.VK_DOWN:
                downPressedActionCallback.execute();
                break;
            case KeyEvent.VK_LEFT:
                leftPressedActionCallback.execute();
                break;
            case KeyEvent.VK_RIGHT:
                rightPressedActionCallback.execute();
                break;
            case KeyEvent.VK_SPACE:
                jumpPressedActionCallback.execute();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upReleaseActionCallback.execute();
                break;
            case KeyEvent.VK_DOWN:
                downReleaseActionCallback.execute();
                break;
            case KeyEvent.VK_LEFT:
                leftReleaseActionCallback.execute();
                break;
            case KeyEvent.VK_RIGHT:
                rightReleaseActionCallback.execute();
                break;
            case KeyEvent.VK_SPACE:
                jumpReleasedActionCallback.execute();
                break;
        }
    }
}
