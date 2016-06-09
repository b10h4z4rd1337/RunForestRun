package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;
import org.jbox2d.common.Vec2;

/**
 * Created by user on 01.06.2016.
 */
public class Sand extends Block {
    public Sand(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    public void setupForLevel(Level level) {
        super.setupForLevel(level);
        setCollisionCallback(new BoxCollisionCallback() {
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
        });
    }

    @Override
    public void render(Renderer renderer) {
        int imageWidth = rectangle.width, imageHeight = rectangle.height;
        if (imageWidth != imageHeight) {
            if (imageWidth > imageHeight) {
                int originX = Math.round(body.getPosition().x * Level.PPM) - rectangle.width / 2 - 1;
                int rest = imageWidth % imageHeight;
                int toDraw = imageWidth - rest;
                int x = originX;
                for (; x < originX + toDraw; x += imageHeight) {
                    renderer.drawImage(x, Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2,
                            rectangle.height, rectangle.height, blockImageName);
                }
                renderer.drawImage(x, Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2,
                        rest, rectangle.height, blockImageName);
            } else {

            }
        } else {
            super.render(renderer);
        }
    }
}

