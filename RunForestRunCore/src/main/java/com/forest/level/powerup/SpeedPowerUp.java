package com.forest.level.powerup;

import com.forest.level.Player;

/**
 * Created by user on 30.05.2016.
 */
public class SpeedPowerUp extends PowerUp {

    public SpeedPowerUp(int x, int y, String powerUpImageName) {
        super(x, y, powerUpImageName);
    }

    @Override
    public void applyPowerUp(Player player) {
        player.setSpeedMultiplier(2f);
    }

}
