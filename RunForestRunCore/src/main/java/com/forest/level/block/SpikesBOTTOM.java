package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by user on 10.06.2016.
 */
public class SpikesBOTTOM extends Block{

    public SpikesBOTTOM(int x, int y, String name) {
        super(x, y, name);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new Block.BoxCollisionCallback() {
            @Override
            public void contact(Block.BoxCollisionData boxCollisionData) {
                if(boxCollisionData.side == Block.BoxCollisionData.BOTTOM){
                    getLevel().setGameOver();
                }
            }

        };
    }
}
