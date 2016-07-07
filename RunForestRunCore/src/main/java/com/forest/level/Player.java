package com.forest.level;

import com.forest.Rectangle;
import com.forest.input.Input;
import com.forest.music.Music;
import com.forest.render.Color;
import com.forest.render.Renderable;
import com.forest.render.Renderer;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by Mathias on 06.05.16.
 */
public class Player implements Renderable {

    public static final int WIDTH = 40, HEIGHT = 75;
    public static final float SPEED_X = 30.f, SPEED_Y = 2000.f;

    private int MAX_JUMPS = 1;
    private int jumpsRemaining = MAX_JUMPS;
    private float jumpMultiplier = 1.f, speedMultiplier = 1.f;

    private Body body;
    private Rectangle rectangle;
    private Level level;
    private String playerImageName;
    private Color color = null;

    private boolean applyRight = false;
    private boolean applyLeft = false;
    private boolean rightApplied = false;
    private boolean leftApplied = false;
    private boolean firstMove = true;
    private boolean onSand = false;

    private long timeToComplete = 0;

    private PositionChangedCallback positionChangedCallback;
    private InputAppliedCallback inputAppliedCallback;

    private Music jumpMusic;

    public Player(int x, int y, String playerImageName, Level level) {
        this.rectangle = new Rectangle(x, y, WIDTH, HEIGHT);
        this.playerImageName = playerImageName;
        this.jumpMusic = Music.MUSIC_FACTORY.createMusic("RFRJump.wav");

        if (level != null)
            setLevel(level);
    }

    public void setLevel(Level level) {
        this.level = level;
        setupPhysics((float)rectangle.x, (float)rectangle.y, (float)WIDTH, (float)HEIGHT, level.getWorld());
    }

    public void setupInput(Input input) {
        if (input != null) {

            input.setPressedUpListener(new Input.ActionCallback() {
                @Override
                public void execute() {

                }
            });
            input.setReleaseUpListener(new Input.ActionCallback() {
                @Override
                public void execute() {

                }
            });

            input.setPressedDownListener(new Input.ActionCallback() {
                @Override
                public void execute() {

                }
            });
            input.setReleaseDownListener(new Input.ActionCallback() {
                @Override
                public void execute() {

                }
            });

            input.setPressedRightListener(new Input.ActionCallback() {
                @Override
                public void execute() {
                    applyRight = true;
                }
            });
            input.setReleaseRightListener(new Input.ActionCallback() {
                @Override
                public void execute() {
                    applyRight = false;
                }
            });

            input.setPressedLeftListener(new Input.ActionCallback() {
                @Override
                public void execute() {
                    applyLeft = true;
                }
            });
            input.setReleaseLeftListener(new Input.ActionCallback() {
                @Override
                public void execute() {
                    applyLeft = false;
                }
            });

            input.setPressedJumpListener(new Input.ActionCallback() {
                @Override
                public void execute() {
                    Player.this.jump();
                }
            });
            input.setReleasedJumpListener(new Input.ActionCallback() {
                @Override
                public void execute() {

                }
            });
        }
    }

    private void setupPhysics(float x, float y, float width, float height, World world) {
        width /= 2;
        height /= 2;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / Level.PPM, height / Level.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = 3;
        fixtureDef.filter.maskBits = 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set((x + width) / Level.PPM, (y + height) / Level.PPM);
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);
        this.body.setTransform(new Vec2((x + width) / Level.PPM, (y + height) / Level.PPM), 0);
        this.body.setUserData(this);
        this.body.createFixture(fixtureDef);
    }

    @Override
    public void render(Renderer renderer) {
        renderer.setCamPos(rectangle.x - 100, renderer.getCamBounds().y);
        update();
        renderer.drawImage(rectangle.x, rectangle.y, rectangle.width, rectangle.height, playerImageName, color);
    }

    private int lastPosX = Integer.MIN_VALUE, lastPosY = Integer.MIN_VALUE;

    private void update() {
        applyInputs();
        this.rectangle.x = Math.round(body.getPosition().x * Level.PPM) - rectangle.width / 2;
        this.rectangle.y = Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2;
        if (rectangle.intersects(level.getEndPoint())) {
            level.receivedEndPoint(this);
        }

        if (positionChangedCallback != null) {
            if (rectangle.x != lastPosX || rectangle.y != lastPosY) {
                positionChangedCallback.positionChanged();
            }
        }
        lastPosX = rectangle.x;
        lastPosY = rectangle.y;

        if (this.rectangle.y < -50) {
            level.setGameOver();
        }
    }

    private void applyInputs() {
        if (applyRight && applyLeft)
            return;

        if (applyRight && (!rightApplied || body.getLinearVelocity().x < SPEED_X * speedMultiplier)) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(SPEED_X * speedMultiplier, vec.y));
            rightApplied = true;

            if (inputAppliedCallback != null)
                inputAppliedCallback.inputApplied();
            checkStopwatch();
        }

        if (!applyRight && rightApplied) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(0.f, vec.y));
            rightApplied = false;

            if (inputAppliedCallback != null)
                inputAppliedCallback.inputApplied();
            checkStopwatch();
        }

        if (applyLeft && (!leftApplied || body.getLinearVelocity().x > -SPEED_X * speedMultiplier)) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(-SPEED_X * speedMultiplier, vec.y));
            leftApplied = true;

            if (inputAppliedCallback != null)
                inputAppliedCallback.inputApplied();
            checkStopwatch();
        }

        if (!applyLeft && leftApplied) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(0.f, vec.y));
            leftApplied = false;

            if (inputAppliedCallback != null)
                inputAppliedCallback.inputApplied();
            checkStopwatch();
        }
    }

    private void checkStopwatch() {
        if (firstMove) {
            firstMove = false;
            level.startStopwatch();
        }
    }

    public void jump() {
        if (jumpsRemaining > 0) {
            if(onSand){
                this.jumpMultiplier = jumpMultiplier * 2;
                this.speedMultiplier = speedMultiplier * 2;
                this.onSand = false;
            }
            body.setLinearVelocity(new Vec2(0, 0));
            body.applyLinearImpulse(new Vec2(0, SPEED_Y * jumpMultiplier), body.getWorldCenter());
            jumpsRemaining--;
            jumpMusic.start();
        }
    }

    public void setOnSand(boolean onSand){ this.onSand = onSand; }

    public boolean isOnSand(){
        return this.onSand;
    }

    public void allowJump() {
        jumpsRemaining = MAX_JUMPS;
    }

    public void setJumpMultiplier(float jumpMultiplier) {
        this.jumpMultiplier = jumpMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public Body getBody() {
        return body;
    }

    public void setTime(long time) {
        this.timeToComplete = time;
    }

    public long getTime() {
        return timeToComplete;
    }

    public void setMaxJumps(int jumpCount) {
        this.MAX_JUMPS = jumpCount;
    }

    public void setPositionChangedCallback(PositionChangedCallback callback) {
        this.positionChangedCallback = callback;
    }

    public void setInpuAppliedCallback(InputAppliedCallback callback) {
        this.inputAppliedCallback = callback;
    }

    public Rectangle getBounds() {
        return rectangle;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Level getLevel() {
        return level;
    }

    public interface PositionChangedCallback {
        void positionChanged();
    }

    public interface InputAppliedCallback {
        void inputApplied();
    }
}
