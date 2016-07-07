package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;
import org.jbox2d.common.Vec2;

/**
 * Created by user on 01.06.2016.
 */
public class Sand extends Block {

    public static final long serialVersionUID = 1338L;

    public Sand(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                    boxCollisionData.player.getBody().setLinearVelocity(new Vec2(0,0));
                    boxCollisionData.player.setOnSand(true);
                    boxCollisionData.player.setSpeedMultiplier(0.5f);
                    boxCollisionData.player.setJumpMultiplier(0.5f);
                }

            }
        };
    }
}

