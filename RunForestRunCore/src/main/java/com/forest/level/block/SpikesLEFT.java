package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by user on 10.06.2016.
 */
public class SpikesLEFT extends Block{

    public static final long serialVersionUID = 1338L;

    public SpikesLEFT(int x, int y, String name) {
        super(x, y, name);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new Block.BoxCollisionCallback() {
            @Override
            public void contact(Block.BoxCollisionData boxCollisionData) {
                if(boxCollisionData.side == Block.BoxCollisionData.LEFT){
                    getLevel().setGameOver();
                }
                if(boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                }
            }

        };
    }
}

