package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 15.05.16.
 */
public class Cloud extends Block {

    public Cloud(int x, int y, String powerUpImageName) {
        super(x, y, powerUpImageName);
    }

    @Override
    public void setupForLevel(final Level level) {
        super.setupForLevel(level);
        this.setCollisionCallback(new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                boxCollisionData.player.allowJump();
                boxCollisionData.player.jump();
                boxCollisionData.player.setSpeedMultiplier(1f);
                level.removeBlock(Cloud.this);
            }
        });
    }

    @Override
    public void render(Renderer renderer) {
        //TODO: Implement general renderer
    }

}