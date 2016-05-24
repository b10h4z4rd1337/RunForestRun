package com.forest.level;

import com.forest.Rectangle;
import com.forest.level.block.Block;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Mathias on 24.05.16.
 */
public class LevelData implements Serializable {

    String backgroundImage = "";
    Rectangle spawnPoint = new Rectangle(0, 0, 0, 0),
            endPoint = new Rectangle(0, 0, 0, 0);
    /**
     * List of Blocks must be sorted by x-coordinate
     */
    LinkedList<Block> blocks = new LinkedList<>();

}
