package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 23.05.16.
 */
public class GroundBlock extends Block {

    public GroundBlock(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    public GroundBlock(int x, int y, int width, int height, String blockImageName) {
        super(x, y, blockImageName);
        setSize(width, height);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                }
            }
        };
    }
}
