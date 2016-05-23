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

    private float jumpMultiplier = 1.f, speedMultiplier = 1.f;
    private Body body;
    private Level level;
    private Rectangle rectangle;
    private String playerImageName;
    private boolean applyRight = false, applyLeft = false, rightApplied = false, leftApplied = false;

    Player(int x, int y, int width, int height, String playerImageName, Input input, Level level) {
        this.rectangle = new Rectangle(x, y, width, height);
        setupPhysics((float)x, (float)y, (float)width, (float)height, level.getWorld());
        this.playerImageName = playerImageName;
        this.level = level;
        setupInput(input);
    }

    public void setupInput(Input input) {
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
        fixtureDef.userData = this;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set((x + width) / Level.PPM, (y + height) / Level.PPM);
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);
        this.body.setTransform(new Vec2((x + width) / Level.PPM, (y + height) / Level.PPM), 0);
        this.body.createFixture(fixtureDef);
    }

    @Override
    public void render(Renderer renderer, long deltaTimeInMs) {
        renderer.drawImage(Math.round(body.getPosition().x * Level.PPM) - rectangle.width / 2,
                Math.round(body.getPosition().y * Level.PPM) - rectangle.height / 2, rectangle.width, rectangle.height, playerImageName);
    }

    void update(Renderer renderer) {
        applyInputs();
        renderer.setCamPos((int)(body.getPosition().x * Level.PPM - rectangle.width / 2) - 100, renderer.getCamBounds().y);
    }

    private void applyInputs() {
        if (applyRight && applyLeft)
            return;

        if (applyRight && (!rightApplied || body.getLinearVelocity().x == 0)) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(10.f * speedMultiplier, vec.y));
            rightApplied = true;
        }

        if (!applyRight && rightApplied) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(0.f, vec.y));
            rightApplied = false;
        }

        if (applyLeft && (!leftApplied || body.getLinearVelocity().x == 0)) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(-10.f * speedMultiplier, vec.y));
            leftApplied = true;
        }

        if (!applyLeft && leftApplied) {
            Vec2 vec = body.getLinearVelocity();
            body.setLinearVelocity(new Vec2(0.f, vec.y));
            leftApplied = false;
        }
    }

    private void jump() {
        body.applyLinearImpulse(new Vec2(0, 700.f * jumpMultiplier), body.getWorldCenter());
    }

    public void setJumpMultiplier(float jumpMultiplier) {
        this.jumpMultiplier = jumpMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
}
