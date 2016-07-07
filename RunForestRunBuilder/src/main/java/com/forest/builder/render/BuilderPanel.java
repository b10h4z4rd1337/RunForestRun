package com.forest.builder.render;

import com.forest.builder.Utils;
import com.forest.input.Input;
import com.forest.input.InputFactory;
import com.forest.level.LevelData;
import com.forest.level.block.Block;
import com.forest.pc.input.PCInput;
import com.forest.pc.render.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mathias on 31.05.16.
 */
public class BuilderPanel extends JPanel {

    private static final int WIDTH = 600, HEIGHT = 400;

    private GameBuilderPanel gameBuilderPanel;
    private BlockList blockList;
    private JSplitPane splitPane;
    private Class<? super Block> blockClassToCreate;
    private String flagToCreate;

    public BuilderPanel(JFrame jframe) {

        JMenuBar bar = new JMenuBar();
        JMenuItem item = new JMenuItem("Run");
        item.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            LevelData data = gameBuilderPanel.toLevelData();
            if (data.endPoint != null && data.spawnPoint != null) {
                GamePanel panel = new GamePanel(data, true);
                Input.FACTORY = new InputFactory() {

                    private PCInput pcInput = null;

                    @Override
                    public Input createInput() {
                        if (pcInput == null)
                            pcInput = new PCInput();
                        panel.addKeyListener(pcInput);
                        return pcInput;
                    }
                };
                JFrame frame = new JFrame("RunForestRun");
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setContentPane(panel);
                frame.pack();
                frame.setVisible(true);
            } else {
                Utils.handleException(jframe, "Must set flags!");
            }
        }));
        bar.add(item);

        JMenu menu = new JMenu("File");
        item = new JMenuItem("Open");
        item.addActionListener(e -> {
            try {
                File file = Utils.openFileOpenDialog(jframe);
                if (file != null) {
                    LevelData levelData = Utils.fileToLevelData(file);
                    gameBuilderPanel.loadFromLevelData(levelData);
                }
            } catch (ClassNotFoundException | IOException e1) {
                Utils.handleException(jframe, e1.getMessage());
            }
        });
        menu.add(item);

        item = new JMenuItem("Save");
        item.addActionListener(e -> {
            LevelData data = gameBuilderPanel.toLevelData();
            if (data.endPoint != null && data.spawnPoint != null) {
                try {
                    File file = Utils.openFileSaveDialog(jframe);
                    if (file != null && !file.getName().endsWith(".rfr")) {
                        file = new File(file.getAbsolutePath() + ".rfr");
                    }
                    if (file != null) {
                        Utils.levelDataToFile(data, file);
                    }
                } catch (IOException e1) {
                    Utils.handleException(jframe, e1.getMessage());
                }
            } else {
                Utils.handleException(jframe, "Must set flags!");
            }
        });
        menu.add(item);

        item = new JMenuItem("Upload");
        item.addActionListener(e -> {
            LevelData data = gameBuilderPanel.toLevelData();
            if (data.endPoint != null && data.spawnPoint != null) {
                try {
                    new Thread(() -> {
                        try {
                            Utils.upload(gameBuilderPanel.toLevelData());
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(jframe, "Successfully uploaded!", "Upload", JOptionPane.INFORMATION_MESSAGE));
                        } catch (Exception e1) {
                            Utils.handleException(jframe, e1.getMessage());
                        }
                    }).start();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                Utils.handleException(jframe, "Must set flags!");
            }
        });
        menu.add(item);

        bar.add(menu);
        jframe.setJMenuBar(bar);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                BuilderPanel.this.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.setLayout(null);
        gameBuilderPanel = new GameBuilderPanel(this);
        gameBuilderPanel.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT));

        try {
            blockList = new BlockList(this);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameBuilderPanel, blockList);
        splitPane.setLocation(0, 0);
        splitPane.setSize(new Dimension(WIDTH, HEIGHT));

        this.add(splitPane);


        try {
            //Utils.upload(Level.createRandomLevel());
            //gameBuilderPanel.loadFromLevelData(Utils.download());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        splitPane.setSize(new Dimension(width, height));
        splitPane.invalidate();
    }

    public void setBlockClassToCreate(Class<? super Block> blockClassToCreate) {
        this.flagToCreate = null;
        this.blockClassToCreate = blockClassToCreate;
        this.blockList.clearFlagList();
    }

    public void setFlag(String flag) {
        this.flagToCreate = flag;
        this.blockClassToCreate = null;
        this.blockList.resetList();
    }

    public Class<? super Block> getBlockClassToCreate() {
        return blockClassToCreate;
    }

    public void setLastSelected(Block lastSelected) {
        blockList.setBlockToEdit(lastSelected);
    }

    public String getFlag() {
        return flagToCreate;
    }
}
