package com.forest.level;

import com.forest.Rectangle;
import com.forest.level.block.Block;
import com.forest.level.block.GroundBlock;
import com.forest.level.powerup.JumpPowerUp;
import com.forest.menu.GameFinishedOverlay;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by Mathias on 04.05.2016.
 */

public class Level implements Renderable {

    public static final float PPM = 10.f;

    //Game Dynamics
    private Rectangle spawnPoint, endPoint;
    private Player player;
    private Stopwatch stopwatch = new Stopwatch();

    //Rendering
    private String backgroundImageName;
    private LinkedList<Block> blocksAfterScope, blocksBeforeScope = new LinkedList<>();
    private LinkedList<Block> blocksInScope = new LinkedList<>();
    private GameFinishedOverlay gameFinishedOverlay;

    //Physics
    private World world;
    private LinkedList<Body> bodiesToRemove = new LinkedList<>();

    public Level(LevelData levelData) {
        initPhysics();
        prepareFromLevelData(levelData);
        spawnPlayer();

        stopwatch.start();
    }

    private void prepareFromLevelData(LevelData levelData) {
        this.backgroundImageName = levelData.backgroundImage;
        this.blocksAfterScope = levelData.blocks;

        for (Block block : blocksAfterScope) {
            block.setupForLevel(this);
        }

        this.spawnPoint = levelData.spawnPoint;
        this.endPoint = levelData.endPoint;
    }

    private void initPhysics() {
        world = new World(new Vec2(0, -70f));
        world.setAllowSleep(true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Player player = null;
                Block block = null;

                if (contact.getFixtureA().getBody().getUserData() instanceof Player && contact.getFixtureB().getBody().getUserData() instanceof Block) {
                    player = (Player) contact.getFixtureA().getBody().getUserData();
                    block = (Block) contact.getFixtureB().getBody().getUserData();
                }

                if (contact.getFixtureB().getBody().getUserData() instanceof Player && contact.getFixtureA().getBody().getUserData() instanceof Block) {
                    player = (Player) contact.getFixtureB().getBody().getUserData();
                    block = (Block) contact.getFixtureA().getBody().getUserData();
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

    private void spawnPlayer() {
        this.player = new Player(spawnPoint.x, spawnPoint.y, 50, 75, "player.png", this);
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

    public Rectangle getEndPoint() {
        return endPoint;
    }

    public void receivedEndPoint(Player player) {
        bodiesToRemove.add(player.getBody());
        player.setTime(stopwatch.getTime());
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
        world.step(deltaT, 3, 8);
        System.out.println(deltaT);
    }

    public void render(Renderer renderer) {
        renderer.drawImagePrivate(0, 0, renderer.getWidth(), renderer.getHeight(), backgroundImageName);

        player.render(renderer);

        for (Block block : blocksInScope) {
            block.render(renderer);
        }

        if (isPlayerFinished()) {
            if (gameFinishedOverlay == null)
                gameFinishedOverlay = new GameFinishedOverlay(renderer, stopwatch.getTimeText());
            gameFinishedOverlay.render(renderer);
            return;
        }

        update((float)renderer.getDeltaTime() / 1000f);
        updateBlocks(renderer.getCamBounds());

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();

        stopwatch.render(renderer);
    }

    private boolean isPlayerFinished() {
        return player.getTime() != 0;
    }

    /**
     * Create Test Level
     */
    public static Level createTestLevel() {
        LevelData levelData = new LevelData();

        //Set Background Image
        levelData.backgroundImage = "background_dark.png";

        //Create Blocks
        for (int i = -100; i < 2000; i += 50) {
            levelData.blocks.add(new GroundBlock(i, 0, 50, 50, "block.png"));
        }
        levelData.blocks.add(new GroundBlock(-100, 0, 2025, 50, "block.png"));

        //Create PowerUp
        levelData.blocks.add(new JumpPowerUp(200, 100, ""));

        //Sort Blocks
        Collections.sort(levelData.blocks, new Comparator<Block>() {
            @Override
            public int compare(Block o1, Block o2) {
                return o1.getX() < o2.getX() ? -1 : 1;
            }
        });
        levelData.spawnPoint = new Rectangle(0, 0, 1, 1);
        levelData.endPoint = new Rectangle(1950, 0, 1, 1000);

        return new Level(levelData);
    }
}
