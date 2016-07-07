package com.forest.input;

/**
 * Created by Mathias on 09.05.16.
 */
public abstract class Input {

    public static InputFactory FACTORY;

    public abstract void setPressedUpListener(ActionCallback actionCallback);
    public abstract void setPressedDownListener(ActionCallback actionCallback);
    public abstract void setPressedLeftListener(ActionCallback actionCallback);
    public abstract void setPressedRightListener(ActionCallback actionCallback);

    public abstract void setReleaseUpListener(ActionCallback actionCallback);
    public abstract void setReleaseDownListener(ActionCallback actionCallback);
    public abstract void setReleaseLeftListener(ActionCallback actionCallback);
    public abstract void setReleaseRightListener(ActionCallback actionCallback);

    public abstract void setPressedJumpListener(ActionCallback actionCallback);
    public abstract void setReleasedJumpListener(ActionCallback actionCallback);

    public interface ActionCallback {
        void execute();
    }
}
