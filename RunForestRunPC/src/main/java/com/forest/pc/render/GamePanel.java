package com.forest.pc.render;

import com.forest.input.Input;
import com.forest.input.InputFactory;
import com.forest.level.Level;
import com.forest.level.LevelData;
import com.forest.menu.MainMenu;
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

    public GamePanel(LevelData level, boolean skip) {
        this.renderer = new PCRenderer(WIDTH, HEIGHT);

        Input.FACTORY = new InputFactory() {

            private PCInput pcInput = null;

            @Override
            public Input createInput() {
                if (pcInput == null) {
                    pcInput = new PCInput();
                    GamePanel.this.addKeyListener(pcInput);
                }
                return pcInput;
            }
        };

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                renderer.setWidth(e.getComponent().getWidth());
                renderer.setHeight(e.getComponent().getHeight());
                renderer.needsUpdate();
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                renderer.touchDown(e.getX(), GamePanel.this.renderer.getHeight() - e.getY());
            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.requestFocus();

        if (skip) {
            this.renderer.addRenderable(new Level(level));
        } else {
            MainMenu menu = new MainMenu(renderer);
            this.renderer.addRenderable(menu);
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
