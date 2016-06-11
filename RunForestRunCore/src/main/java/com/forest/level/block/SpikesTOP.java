package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by user on 06.06.2016.
 */
public class SpikesTOP extends Block {

    public SpikesTOP(int x, int y, String name) {
        super(x, y, name);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if(boxCollisionData.side == BoxCollisionData.TOP){
                    getLevel().setGameOver();
                }
            }
        };
    }
}
