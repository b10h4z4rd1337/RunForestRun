package com.forest.level;

import com.forest.Rectangle;
import com.forest.input.Input;
import com.forest.render.Renderable;
import com.forest.render.Renderer;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * Created by Mathias on 06.05.16.
 */
public class Player implements Renderable {

    public static final float SPEED_X = 30.f, SPEED_Y = 2000.f;

    private int MAX_JUMPS = 1;
    private int jumpsRemaind = MAX_JUMPS;
    private float jumpMultiplier = 1.f, speedMultiplier = 1.f;

    private Body body;
    private Rectangle rectangle;
    private Level level;
    private String playerImageName;

    private boolean applyRight = false, applyLeft = false, rightApplied = false,
            leftApplied = false, inputApplied = false, firstMove = true;

    private long timeToComplete = 0;

    Player(int x, int y, int width, int height, String playerImageName, Level level) {
        this.rectangle = new Rectangle(x, y, width, height);
        setupPhysics((float)x, (float)y, (float)width, (float)height, level.getWorld());
        this.playerImageName = playerImageName;
        this.level = level;
    }

    private void setupInput(Input input) {
        if (input != null) {
            //Setup input
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
            inputApplied = true;
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
        if (!inputApplied)
            setupInput(renderer.getInput());
        update(renderer);
        renderer.drawImage(rectangle.x, rectangle.y, rectangle.width, rectangle.height, playerImageName);
    }

    private void update(Renderer renderer) {
        applyInputs();
        this.rectangle.x = Math.round(body.getPosition().x * Level.PPM) - rectangle.width / 2;
        this.rectangle.y = Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2;
        renderer.setCamPos(rectangle.x - 100, renderer.getCamBounds().y);
        if (rectangle.intersects(level.getEndPoint())) {
            level.receivedEndPoint(this);
        }
    }

    private void applyInputs() {
        if (applyRight && applyLeft)
            return;

        if (applyRight && (!rightApplied || body.getLinearVelocity().x < SPEED_X * speedMultiplier)) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(SPEED_X * speedMultiplier, vec.y));
            rightApplied = true;

            checkStopwatch();
        }

        if (!applyRight && rightApplied) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(0.f, vec.y));
            rightApplied = false;

            checkStopwatch();
        }

        if (applyLeft && (!leftApplied || body.getLinearVelocity().x > -SPEED_X * speedMultiplier)) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(-SPEED_X * speedMultiplier, vec.y));
            leftApplied = true;

            checkStopwatch();
        }

        if (!applyLeft && leftApplied) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(0.f, vec.y));
            leftApplied = false;
        }
    }

    private void checkStopwatch() {
        if (firstMove) {
            firstMove = false;
            level.startStopwatch();
        }
    }

    private void jump() {
        if (jumpsRemaind > 0) {
            body.setLinearVelocity(new Vec2(0, 0));
            body.applyLinearImpulse(new Vec2(0, SPEED_Y * jumpMultiplier), body.getWorldCenter());
            jumpsRemaind--;
        }
    }

    public void allowJump() {
        jumpsRemaind = MAX_JUMPS;
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
}
