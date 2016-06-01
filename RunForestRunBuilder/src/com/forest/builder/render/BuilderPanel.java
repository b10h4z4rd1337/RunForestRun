package com.forest.builder.render;

import com.forest.pc.render.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

/**
 * Created by Mathias on 31.05.16.
 */
public class BuilderPanel extends JPanel {

    private static final int WIDTH = 600, HEIGHT = 400;

    private GamePanel gamePanel;
    private BlockList blockList;
    private JSplitPane splitPane;

    public BuilderPanel() {
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                BuilderPanel.this.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.setLayout(null);
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT));

        try {
            blockList = new BlockList();
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
}
