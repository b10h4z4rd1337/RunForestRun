package com.forest.level.powerup;

import com.forest.level.Level;
import com.forest.level.Player;
import com.forest.level.block.Block;
import com.forest.render.*;

/**
 * Created by Mathias on 15.05.16.
 */
public abstract class PowerUp extends Block {

    private static int WIDTH = 26, HEIGHT = 26;

    public PowerUp(int x, int y, String powerUpImageName) {
        super(x, y, powerUpImageName);
        setSize(WIDTH, HEIGHT);
    }
/*
    @Override
    public void render(Renderer renderer) {
        if (getBlockImageName().isEmpty())
            renderer.drawRect(Math.round(getBody().getPosition().x * Level.PPM) - getBounds().width / 2,
                Math.round(getBody().getPosition().y * Level.PPM) - getBounds().height / 2, getBounds().width, getBounds().height, new com.forest.render.Color(0, 0, 0));
        else
            renderer.drawImage(Math.round(getBody().getPosition().x * Level.PPM) - getBounds().width / 2,
                    Math.round(getBody().getPosition().y * Level.PPM) - getBounds().height / 2, getBounds().width, getBounds().height, getBlockImageName());
    }
*/
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
