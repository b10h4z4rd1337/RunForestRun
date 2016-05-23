package com.forest.level;

import com.forest.Rectangle;
import com.forest.input.Input;
import com.forest.level.block.Block;
import com.forest.level.block.GroundBlock;
import com.forest.level.powerup.JumpPowerUp;
import com.forest.level.powerup.PowerUp;
import com.forest.render.Renderable;
import com.forest.render.Renderer;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by Mathias on 04.05.2016.
 */

public class Level implements Serializable, Renderable {

    public static final float PPM = 10.f;

    private String backgroundImageName;

    private LinkedList<Player> players;

    private LinkedList<Body> bodiesToRemove;

    private LinkedList<Block> blocksAfterScope, blocksBeforeScope;
    private LinkedList<Block> blocksInScope;


    //Physics
    private World world;

    public Level(String backgroundImageName) {
        this.backgroundImageName = backgroundImageName;
        blocksAfterScope = new LinkedList<>();
        blocksBeforeScope = new LinkedList<>();
        blocksInScope = new LinkedList<>();
        bodiesToRemove = new LinkedList<>();

        players = new LinkedList<>();

        world = new World(new Vec2(0, -10f));

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Player player = null;
                Block block = null;

                if (contact.getFixtureA().getUserData() instanceof Player && contact.getFixtureB().getUserData() instanceof Block) {
                    player = (Player) contact.getFixtureA().getUserData();
                    block = (Block) contact.getFixtureB().getUserData();
                }

                if (contact.getFixtureB().getUserData() instanceof Player && contact.getFixtureA().getUserData() instanceof Block) {
                    player = (Player) contact.getFixtureB().getUserData();
                    block = (Block) contact.getFixtureA().getUserData();
                }

                if (block != null) {
                    WorldManifold worldManifold = new WorldManifold();
                    contact.getWorldManifold(worldManifold);

                    int side = 0;

                    if (worldManifold.normal.x > 0) {
                        side = Block.BoxCollisionData.RIGHT;
                    } else if (worldManifold.normal.x < 0) {
                        side = Block.BoxCollisionData.LEFT;
                    }

                    if (worldManifold.normal.y > 0) {
                        side = Block.BoxCollisionData.TOP;
                    } else if (worldManifold.normal.y < 0) {
                        side = Block.BoxCollisionData.BOTTOM;
                    }

                    block.getCollisionCallback().contact(new Block.BoxCollisionData(player, side));
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });
    }

    public void removeBlock(Block block) {
        int index;
        if ((index = blocksBeforeScope.indexOf(block)) != -1) {
            blocksBeforeScope.remove(index);
        }

        if ((index = blocksInScope.indexOf(block)) != -1) {
            blocksInScope.remove(index);
        }

        if ((index = blocksAfterScope.indexOf(block)) != -1) {
            blocksAfterScope.remove(index);
        }

        bodiesToRemove.add(block.getBody());
    }

    public World getWorld() {
        return world;
    }

    public Player createPlayer(int x, int y, int width, int height, String playerImageName, Input input) {
        Player player = new Player(x, y, width, height, playerImageName, input, this);
        players.add(player);
        return player;
    }

    public Block createGroundBlock(int x, int y, int width, int height, String blockImageName) {
        Block block = new GroundBlock(x, y, width, height, blockImageName, this);
        blocksAfterScope.add(block);
        Collections.sort(blocksAfterScope, new Comparator<Block>() {
            @Override
            public int compare(Block o1, Block o2) {
                return o1.getX() < o2.getX() ? -1 : 1;
            }
        });
        return block;
    }

    public PowerUp createJumpPowerUp(int x, int y, String imageName) {
        PowerUp powerUp = new JumpPowerUp(x, y, imageName, this);
        blocksAfterScope.add(powerUp);
        Collections.sort(blocksAfterScope, new Comparator<Block>() {
            @Override
            public int compare(Block o1, Block o2) {
                return o1.getX() < o2.getX() ? -1 : 1;
            }
        });
        return powerUp;
    }

    private int lastCamX = Integer.MIN_VALUE;

    private void updateBlocks(Rectangle camBounds) {
        if (lastCamX < camBounds.x) {
            while (!blocksInScope.isEmpty() && !blocksInScope.peekFirst().getBounds().intersects(camBounds)) {
                blocksBeforeScope.add(blocksInScope.removeFirst());
            }

            while (!blocksAfterScope.isEmpty() && blocksAfterScope.peekFirst().getBounds().intersects(camBounds)) {
                blocksInScope.add(blocksAfterScope.removeFirst());
            }
        } else if (lastCamX > camBounds.x){
            while (!blocksInScope.isEmpty() && !blocksInScope.peekLast().getBounds().intersects(camBounds)) {
                blocksAfterScope.add(0, blocksInScope.removeLast());
            }

            while (!blocksBeforeScope.isEmpty() && blocksBeforeScope.peekLast().getBounds().intersects(camBounds)) {
                blocksInScope.add(0, blocksBeforeScope.removeLast());
            }
        }
        lastCamX = camBounds.x;
    }

    private void update(float deltaT) {
        world.step(60.f / 1000.f, 3, 8);
    }

    /*
     * Render Level: Player must be rendered first, because it has to apply camera updates which effect block rendering
     */

    public void render(Renderer renderer, long deltaTimeInMs) {
        renderer.drawImagePrivate(0, 0, renderer.getWidth(), renderer.getHeight(), backgroundImageName);

        for (Player player : players) {
            player.update(renderer);
            player.render(renderer, deltaTimeInMs);
        }

        update((float)deltaTimeInMs / 1000f);
        updateBlocks(renderer.getCamBounds());

        for (Block block : blocksInScope) {
            block.render(renderer, deltaTimeInMs);
        }

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();
    }

    /*
     * Create Test Level
     */
    public static Level createTestLevel() {
        Level level = new Level("background_dark.png");
        for (int i = -100; i < 2000; i += 50) {
            level.createGroundBlock(i, 0, 50, 50, "block.png");
        }
        level.createGroundBlock(100, 50, 50, 50, "block.png");
        level.createGroundBlock(150, 100, 50, 50, "block.png");
        level.createGroundBlock(200, 150, 50, 50, "block.png");
        level.createGroundBlock(0, 0, 50, 50, "block.png");
        level.createJumpPowerUp(100, 100, "");
        level.createPlayer(0, 50, 50, 80, "player.png", null);

        return level;
    }
}
