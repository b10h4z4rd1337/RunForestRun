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

    public Slime(int x, int y, int width, int height, String blockImageName) {
        super(x, y, blockImageName);
    }

    @Override
    public void setupForLevel(Level level) {
        super.setupForLevel(level);
        setCollisionCallback(new BoxCollisionCallback() {
            @Override
            public void contact(BoxCollisionData boxCollisionData) {
                if (boxCollisionData.side == BoxCollisionData.TOP) {
                    boxCollisionData.player.allowJump();
                    boxCollisionData.player.setSpeedMultiplier(1f);
                }
            }
        });
    }

    @Override
    void setupPhysics(float x, float y, float width, float height, World world) {
        width /= 2;
        height /= 2;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / Level.PPM, height / Level.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.8f;
        fixtureDef.filter.categoryBits = 3;
        fixtureDef.filter.maskBits = 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set((x + width) / Level.PPM, (y + height) / Level.PPM);
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.createFixture(fixtureDef);
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

