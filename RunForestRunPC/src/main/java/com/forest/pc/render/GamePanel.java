package com.forest.pc.render;

import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.pc.input.PCInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Mathias on 04.05.16.
 */
public class GamePanel extends JPanel {

    private static final int WIDTH = 600, HEIGHT = 400;

    private PCRenderer renderer;
    private com.forest.menu.Menu menu;

    public GamePanel(LevelData levelData, boolean skip) {
        PCInput pcInput = new PCInput();
        this.renderer = new PCRenderer(WIDTH, HEIGHT, pcInput);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                renderer.setWidth(e.getComponent().getWidth());
                renderer.setHeight(e.getComponent().getHeight());
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu != null)
                    menu.performClick(e.getX(), GamePanel.this.renderer.getHeight() - e.getY());
            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.addKeyListener(pcInput);
        this.setFocusable(true);
        this.requestFocus();

        if (skip) {
            this.renderer.addRenderable(new Level(levelData));
        } else {
            this.menu = new com.forest.menu.MainMenu(renderer);
            this.renderer.addRenderable(this.menu);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        renderer.setGraphics(g);
        renderer.render();
        repaint();
    }
}
