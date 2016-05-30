package com.forest.level.powerup;

import com.forest.level.Player;

/**
 * Created by user on 30.05.2016.
 */
public class DoubleJumpPowerUp extends PowerUp {

    public DoubleJumpPowerUp(int x, int y, String powerUpImageName) {
        super(x, y, powerUpImageName);
    }

    @Override
    public void applyPowerUp(Player player) {
        player.setMaxJumps(2);
    }
}
