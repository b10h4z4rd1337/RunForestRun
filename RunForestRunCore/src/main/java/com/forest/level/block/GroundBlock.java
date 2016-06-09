package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;

/**
 * Created by Mathias on 23.05.16.
 */
public class GroundBlock extends Block {

    public GroundBlock(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    public GroundBlock(int x, int y, int width, int height, String blockImageName) {
        super(x, y, blockImageName);
        setSize(width, height);
    }

    @Override
    public void setupForLevel(Level level) {
        super.setupForLevel(level);
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
        setRectangleLocation();
        if (imageWidth != imageHeight) {
            if (imageWidth > imageHeight) {
                int originX = rectangle.x;
                int rest = imageWidth % imageHeight;
                int toDraw = imageWidth - rest;
                int x = originX;
                for (; x < originX + toDraw; x += imageHeight) {
                    renderer.drawImage(x, rectangle.y, rectangle.height, rectangle.height, blockImageName);
                }
                renderer.drawImage(x, rectangle.y, rest, rectangle.height, blockImageName);
            } else {
                //TODO: Implement same for height
            }
        } else {
            super.render(renderer);
        }
    }
}
