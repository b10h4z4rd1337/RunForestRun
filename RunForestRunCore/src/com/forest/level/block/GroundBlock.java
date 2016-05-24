package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 23.05.16.
 */
public class GroundBlock extends Block {

    public GroundBlock(int x, int y, int width, int height, String blockImageName) {
        super(x, y, width, height, blockImageName);
        setCollisionCallback(new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
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
