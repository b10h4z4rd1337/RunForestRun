package com.forest.level.block;

import com.forest.Rectangle;
import com.forest.level.Level;
import com.forest.level.Player;
import com.forest.render.Renderable;
import com.forest.render.Renderer;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.*;

import java.io.Serializable;

/**
 * Created by Mathias on 06.05.16.
 */
public abstract class Block implements Renderable, Serializable {

    public static final long serialVersionUID = 1338L;
    public static final int BLOCK_SIDE = 50;

    private Rectangle rectangle;
    private String blockImageName;
    private transient BoxCollisionCallback collisionCallback;
    private transient Body body;
    private transient Level level;
    private int id = -1;

    public Block(int x, int y, String blockImageName) {
        this.rectangle = new Rectangle(x, y, BLOCK_SIDE, BLOCK_SIDE);
        this.blockImageName = blockImageName;
    }

    public void setupForLevel(final Level level) {
        this.collisionCallback = generateBoxCollisionCallback();
        setupPhysics(rectangle.x, rectangle.y, rectangle.width, rectangle.height, level.getWorld());
        this.level = level;
    }

    void setupPhysics(float x, float y, float width, float height, World world) {
        width /= 2;
        height /= 2;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / Level.PPM, height / Level.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = getRestitution();
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

    void setRectangleLocation() {
        if (body != null) {
            rectangle.x = Math.round(body.getPosition().x * Level.PPM) - rectangle.width / 2;
            rectangle.y = Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2;
        }
    }

    @Override
    public void render(Renderer renderer) {
        setRectangleLocation();

        int imageWidth = rectangle.width, imageHeight = rectangle.height;
        int biggerVal = Math.max(imageWidth, imageHeight);
        int otherVal = imageWidth == biggerVal ? imageHeight : imageWidth;

        int origin = Math.round(imageWidth >= imageHeight ? rectangle.x : rectangle.y);
        int rest = biggerVal % otherVal;
        int toDraw = biggerVal - rest;
        int pos = origin;
        for (; pos < origin + toDraw; pos += otherVal) {
            if (imageWidth >= imageHeight) {
                renderer.drawImage(pos, rectangle.y,
                        imageHeight, imageHeight, blockImageName);
            } else {
                renderer.drawImage(rectangle.x, pos,
                        imageWidth, imageWidth, blockImageName);
            }
        }

        if (imageWidth > imageHeight) {
            renderer.drawImage(pos, rectangle.y,
                    rest, imageHeight, blockImageName);
        } else {
            renderer.drawImage(rectangle.x - rectangle.width / 2, pos,
                    imageWidth, rest, blockImageName);
        }

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

    public String getBlockImageName() {
        return blockImageName;
    }

    public Level getLevel() {
        return level;
    }

    public void setLocation(int x, int y) {
        this.rectangle.x = x;
        this.rectangle.y = y;
    }

    public void setSize(int width, int height) {
        this.rectangle.width = width;
        this.rectangle.height = height;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.setLocation(x, y);
        this.setSize(width, height);
    }

    public abstract BoxCollisionCallback generateBoxCollisionCallback();

    public float getRestitution() {
        return 0f;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public int getID() {
        return id;
    }

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