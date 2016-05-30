package com.forest.builder;

import com.forest.level.LevelData;
import com.forest.level.block.Block;
import com.forest.level.block.GroundBlock;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by user on 30.05.2016.
 */
public class Main {

    public static void main(String[] args) {
        LevelData level = new LevelData();
        level.backgroundImage = "Hello";
        level.blocks.add(new GroundBlock(50, 50, 50, 50, ""));
        Collections.sort(level.blocks, new Comparator<Block>() {
            @Override
            public int compare(Block o1, Block o2) {
                return o1.getX() < o2.getX() ? -1 : 1;
            }
        });

        try {
            FileOutputStream fos = new FileOutputStream(new File("C:/Users/User/level.rfr"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(level);

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("C:/Users/User/level.rfr")));
            LevelData ld = (LevelData) ois.readObject();
            System.out.println(level.backgroundImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
