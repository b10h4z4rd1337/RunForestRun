package com.forest.level.block;

import com.forest.level.Level;
import com.forest.render.Renderer;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Created by user on 01.06.2016.
 */
public class Slime extends Block {

    public Slime(int x, int y, String blockImageName) {
        super(x, y, blockImageName);
    }

    @Override
    public BoxCollisionCallback generateBoxCollisionCallback() {
        return new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                }
            }
        };
    }

    @Override
    public float getRestitution() {
        return 0.8f;
    }

}

