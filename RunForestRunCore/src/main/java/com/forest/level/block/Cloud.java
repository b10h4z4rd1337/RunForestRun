package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 15.05.16.
 */
public class Cloud extends Block {

    public static final long serialVersionUID = 1338L;

    public Cloud(int x, int y, String name) {
        super(x, y, name);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                boxCollisionData.player.allowJump();
                boxCollisionData.player.jump();
                boxCollisionData.player.setSpeedMultiplier(1f);
                getLevel().removeBlock(Cloud.this);
            }
        };
    }

}