package com.forest.builder.render;

import com.forest.level.block.Block;
import com.forest.pc.render.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by Mathias on 31.05.16.
 */
public class BuilderPanel extends JPanel {

    private static final int WIDTH = 600, HEIGHT = 400;

    // TODO: Implement Debug Gamepanel with Grid
    private GamePanel gamePanel;
    private BlockList blockList;
    private JSplitPane splitPane;
    private Block blockToCreate;

    public BuilderPanel() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                BuilderPanel.this.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (blockToCreate != null) {
                    //TODO: Set bounds
                }
            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.setLayout(null);
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT));

        try {
            blockList = new BlockList(this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gamePanel, blockList);
        splitPane.setLocation(0, 0);
        splitPane.setSize(new Dimension(WIDTH, HEIGHT));

        this.add(splitPane);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        splitPane.setSize(new Dimension(width, height));
        splitPane.invalidate();
    }

    public void setBlockToCreate(Block block) {
        this.blockToCreate = block;
    }

    public Block getBlockToCreate() {
        return blockToCreate;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (blockToCreate != null) {
            //TODO: Implement mouse drag
        }
    }
}
