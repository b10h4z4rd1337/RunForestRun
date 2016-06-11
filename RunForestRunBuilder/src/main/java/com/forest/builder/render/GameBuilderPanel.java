package com.forest.builder.render;

import com.forest.builder.BuilderLevel;
import com.forest.builder.Utils;
import com.forest.level.LevelData;
import com.forest.level.block.Block;
import com.forest.pc.render.PCRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Mathias on 07.06.16.
 */
class GameBuilderPanel extends JPanel {

    private static final int WIDTH = 600, HEIGHT = 400;

    private PCRenderer renderer;
    private BuilderLevel builderLevel;
    private BuilderPanel builderPanel;

    private Block selected;

    GameBuilderPanel(BuilderPanel builderPanel) {
        this.builderPanel = builderPanel;
        this.builderLevel = new BuilderLevel();
        this.builderLevel.setBackgroundImageName("Background_dark.png");

        this.renderer = new PCRenderer(WIDTH, HEIGHT, null);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                renderer.setWidth(e.getComponent().getWidth());
                renderer.setHeight(e.getComponent().getHeight());
                builderLevel.resize();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Block rayResult = builderLevel.ray(e.getX() + renderer.getCamBounds().x, renderer.getHeight() - e.getY());
                if (rayResult != null) {
                    selected = rayResult;
                } else {
                    if (builderPanel.getFlag() == null) {
                        try {
                            selected = Utils.createBlockFromClass(builderPanel.getBlockClassToCreate());
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e1) {
                            e1.printStackTrace();
                        }
                        if (selected != null) {
                            int x = e.getX() + renderer.getCamBounds().x;
                            x = x - (x % Block.BLOCK_SIDE);
                            int y = renderer.getHeight() - e.getY();
                            y = y - (y % Block.BLOCK_SIDE);
                            selected.setLocation(x, y);
                            builderLevel.addBlock(selected);
                        }
                    } else {
                        String flag = builderPanel.getFlag();
                        int x = e.getX() + renderer.getCamBounds().x;
                        if (flag.equals("START")) {
                            builderLevel.setStartPoint(x);
                        } else {
                            builderLevel.setEndPoint(x);
                        }
                    }
                }
                builderPanel.setLastSelected(selected);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selected = null;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selected != null) {
                    int x = e.getX() + renderer.getCamBounds().x;
                    x = x - (x % Block.BLOCK_SIDE);
                    int y = renderer.getHeight() - e.getY();
                    y = y - (y % Block.BLOCK_SIDE);
                    selected.setLocation(x, y);
                }
            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);

        this.renderer.addRenderable(builderLevel);
    }

    LevelData toLevelData() {
        return builderLevel.toLevelData();
    }

    void loadFromLevelData(LevelData levelData) {
        builderLevel.loadFromLevelData(levelData);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        renderer.setGraphics(g);
        renderer.render();
        builderPanel.repaint();
    }
}
