package com.forest.pc.render;

import com.forest.input.Input;
import com.forest.level.Level;
import com.forest.menu.*;
import com.forest.pc.Main;
import com.forest.pc.input.PCInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Mathias on 04.05.16.
 */
public class GamePanel extends JPanel {

    public static final int WIDTH = 600, HEIGHT = 400;
    private PCRenderer renderer;
    private PCInput pcInput;
    private com.forest.menu.Menu menu;

    public GamePanel() {
        this.pcInput = new PCInput();
        this.renderer = new PCRenderer(WIDTH, HEIGHT, pcInput);
        SwingUtilities.invokeLater(GamePanel.this::init);
    }

    private void init() {
        JFrame frame = new JFrame("RunForestRun");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                renderer.setWidth(e.getComponent().getWidth());
                renderer.setHeight(e.getComponent().getHeight());
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
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu != null)
                    menu.performClick(e.getX(), GamePanel.this.renderer.getHeight() - e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.addKeyListener(pcInput);
        this.setFocusable(true);
        this.requestFocus();
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);

        boolean skip = false;

        if (skip) {
            Level level = Level.createTestLevel();
            level.createPlayer(0, 50, 50, 80, "player.png", pcInput);
            this.renderer.addRenderable(level);
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
