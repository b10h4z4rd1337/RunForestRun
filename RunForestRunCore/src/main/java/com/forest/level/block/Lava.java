package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by user on 03.06.2016.
 */
public class Lava extends Block {

    public Lava(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    @Override
    public void setupForLevel(final Level level) {
        super.setupForLevel(level);
        setCollisionCallback(new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                    level.setGameOver();
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

