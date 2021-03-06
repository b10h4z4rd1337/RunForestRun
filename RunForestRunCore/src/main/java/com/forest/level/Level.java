package com.forest.level;

import com.forest.Rectangle;
import com.forest.input.Input;
import com.forest.level.block.Block;
import com.forest.level.block.Cloud;
import com.forest.level.block.GroundBlock;
import com.forest.level.powerup.DoubleJumpPowerUp;
import com.forest.level.powerup.SpeedPowerUp;
import com.forest.menu.GameFinishedOverlay;
import com.forest.menu.GameOverOverlay;
import com.forest.music.Music;
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
    public static final Comparator<Block> BLOCK_COMPARATOR = new Comparator<Block>() {
        @Override
        public int compare(Block o1, Block o2) {
            return o1.getX() < o2.getX() ? -1 : 1;
        }
    };

    //Game Dynamics
    private Rectangle spawnPoint, endPoint;
    protected Player player;
    protected Stopwatch stopwatch = new Stopwatch();
    protected boolean gameOver = false;
    private Music levelMusic;

    //Rendering
    private String backgroundImageName;
    protected volatile LinkedList<Block> blocksAfterScope;
    protected volatile LinkedList<Block> blocksBeforeScope = new LinkedList<>();
    protected volatile LinkedList<Block> blocksInScope = new LinkedList<>();
    private GameFinishedOverlay gameFinishedOverlay;
    private GameOverOverlay gameOverOverlay;

    //Physics
    protected World world;
    protected LinkedList<Body> bodiesToRemove = new LinkedList<>();

    public Level(LevelData levelData) {
        initPhysics();
        prepareFromLevelData(levelData);
        spawnPlayer();
        levelMusic = Music.MUSIC_FACTORY.createMusic("RFR1.wav");
        levelMusic.loop();
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
        setPlayer(new Player(spawnPoint.x, spawnPoint.y, "player.png", this));
        this.player.setupInput(Input.FACTORY.createInput());
    }

    public void setPlayer(Player player) {
        player.setLevel(this);
        this.player = player;
    }

    public void removeBlock(Block block) {
        if (!blocksInScope.remove(block))
            if (!blocksBeforeScope.remove(block))
                blocksAfterScope.remove(block);

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

    public void startStopwatch() {
        stopwatch.start();
    }


    private int lastCamX = Integer.MIN_VALUE;

    //TODO: Fix renderer
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

    protected void removeBlocksUpdate() {

    }

    private void update(float deltaT) {
        world.step(deltaT, 3, 8);
    }

    private boolean firstRender = true;

    public void render(Renderer renderer) {
        if (firstRender) {
            renderer.setCamPos(spawnPoint.x - 100, spawnPoint.y);
            firstRender = false;
        }
        if (renderer.isUpdateNeeded()) {
            this.lastCamX = Integer.MIN_VALUE;
            blocksAfterScope.addAll(0, blocksInScope);
            blocksInScope.clear();
            blocksAfterScope.addAll(0, blocksBeforeScope);
            blocksBeforeScope.clear();
        }
        if(gameOver){
            if (gameOverOverlay == null)
                gameOverOverlay = new GameOverOverlay(renderer, stopwatch.getTimeText());
            gameOverOverlay.render(renderer);
            return;
        }

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

        removeBlocksUpdate();

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();

        stopwatch.render(renderer);
    }

    private boolean isPlayerFinished() {
        return player.getTime() != 0;
    }

    public void setGameOver(){
        this.gameOver = true;
        this.levelMusic.stop();
    }

    /**
     * Create Test Level
     */
    public static LevelData createTestLevel() {
        LevelData levelData = new LevelData();

        //Set Background Image
        levelData.backgroundImage = "Ghosts mit Wald.png";

        levelData.blocks.add(new GroundBlock(-100, 0, 2025, 50, "GroundBlock.png"));

        //Create PowerUp
        levelData.blocks.add(new DoubleJumpPowerUp(200, 100, "DoubleJumpPowerUp.png"));
        levelData.blocks.add(new SpeedPowerUp(300, 200, "SpeedPowerUp.png"));

        //Sort Blocks
        Collections.sort(levelData.blocks, Level.BLOCK_COMPARATOR);
        levelData.spawnPoint = new Rectangle(0, 0, 1, 1);
        levelData.endPoint = new Rectangle(1950, 0, 1, 1000);

        return levelData;
    }

    public static LevelData createRandomLevel() {
        LevelData levelData = new LevelData();

        //Set Background Image
        levelData.backgroundImage = "Ghosts mit Wald.png";

        levelData.blocks.add(new GroundBlock(-100, 0, 1600, 50, "GroundBlock.png"));

        //Create PowerUp


        //add new test blocks

        for(int j= 50; j<1800; j+=100){
            double randomBlock = Math.random();
            if(randomBlock<0.5){
                int k = (int) Math.round(Math.random() * 5);
                levelData.blocks.add(new Cloud(j, 50 * (k + 1), "Cloud.png"));
            }
            else{
                int k = (int) Math.round(Math.random() * 5);
                levelData.blocks.add(new GroundBlock(j, 50 * (k + 1), 50, 50, "GroundBlock.png"));
            }
        }

        //Sort Blocks
        Collections.sort(levelData.blocks, Level.BLOCK_COMPARATOR);
        levelData.spawnPoint = new Rectangle(0, 0, 1, 1);
        levelData.endPoint = new Rectangle(1950, 0, 1, 1000);

        return levelData;
    }
}
