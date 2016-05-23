package com.forest.pc;

import com.forest.level.Level;
import com.forest.pc.render.GamePanel;

public class Main {
    public static void main(String[] args) {
        new GamePanel();
    }

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

        return level;
    }
}
