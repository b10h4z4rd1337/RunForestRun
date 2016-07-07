package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by user on 03.06.2016.
 */
public class Lava extends Block {

    public static final long serialVersionUID = 1338L;

    public Lava(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                    getLevel().setGameOver();
                }
            }
        };
    }
}

