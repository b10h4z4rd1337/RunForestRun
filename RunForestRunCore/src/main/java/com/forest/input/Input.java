package com.forest.input;

/**
 * Created by Mathias on 09.05.16.
 */
public interface Input {
    void setPressedUpListener(ActionCallback actionCallback);
    void setPressedDownListener(ActionCallback actionCallback);
    void setPressedLeftListener(ActionCallback actionCallback);
    void setPressedRightListener(ActionCallback actionCallback);

    void setReleaseUpListener(ActionCallback actionCallback);
    void setReleaseDownListener(ActionCallback actionCallback);
    void setReleaseLeftListener(ActionCallback actionCallback);
    void setReleaseRightListener(ActionCallback actionCallback);

    void setPressedJumpListener(ActionCallback actionCallback);
    void setReleasedJumpListener(ActionCallback actionCallback);

    @FunctionalInterface
    interface ActionCallback {
        void execute();
    }
}
