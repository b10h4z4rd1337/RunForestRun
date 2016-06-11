package com.forest.level.powerup;

import com.forest.level.Player;
import com.forest.level.block.Block;

/**
 * Created by Mathias on 15.05.16.
 */
public abstract class PowerUp extends Block {

    private static final int WIDTH = 26, HEIGHT = 26;

    PowerUp(int x, int y, String powerUpImageName) {
        super(x, y, powerUpImageName);
        setSize(WIDTH, HEIGHT);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                getLevel().removeBlock(PowerUp.this);
                applyPowerUp(boxCollisionData.player);
            }
        };
    }

    public abstract void applyPowerUp(Player player);
}
