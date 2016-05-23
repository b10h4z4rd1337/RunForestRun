package com.forest.level.block;

import com.forest.level.Level;
import com.forest.level.Player;
import com.forest.render.Renderable;
import com.forest.render.Renderer;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;

import java.awt.*;

/**
 * Created by Mathias on 06.05.16.
 */
public abstract class Block implements Renderable {

    private Rectangle rectangle;
    private Body body;
    private Level level;
    private String blockImageName;
    private BoxCollisionCallback collisionCallback;

    public Block(int x, int y, int width, int height, String blockImageName, Level level) {
        this.rectangle = new Rectangle(x, y, width, height);
        setupPhysics(x, y, width, height, level.getWorld());
        this.blockImageName = blockImageName;
        this.collisionCallback = boxCollisionData -> { };
        this.level = level;
    }

    private void setupPhysics(float x, float y, float width, float height, World world) {
        width /= 2;
        height /= 2;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / Level.PPM, height / Level.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = 3;
        fixtureDef.filter.maskBits = 2;
        fixtureDef.userData = this;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set((x + width) / Level.PPM, (y + height) / Level.PPM);
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
    }

    @Override
    public void render(Renderer renderer, long deltaTimeInMs) {
        renderer.drawImage(Math.round(body.getPosition().x * Level.PPM) - rectangle.width / 2,
                Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2, rectangle.width, rectangle.height, blockImageName);
    }

    public void setCollisionCallback(BoxCollisionCallback boxCollisionCallback) {
        this.collisionCallback = boxCollisionCallback;
    }

    public BoxCollisionCallback getCollisionCallback() {
        return collisionCallback;
    }

    public Rectangle getBounds() {
        return rectangle;
    }

    public int getX() {
        return rectangle.x;
    }

    public Body getBody() {
        return body;
    }

    Level getLevel() {
        return level;
    }

    @FunctionalInterface
    public interface BoxCollisionCallback {
        void contact(BoxCollisionData boxCollisionData);
    }

    public static class BoxCollisionData {
        public static int LEFT = 1, RIGHT = 2, BOTTOM = 3, TOP = 4;
        public Player player;
        public int side;

        public BoxCollisionData(Player player, int side) {
            this.side = side;
            this.player = player;
        }
    }
}
