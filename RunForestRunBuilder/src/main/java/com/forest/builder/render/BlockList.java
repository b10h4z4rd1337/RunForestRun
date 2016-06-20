package com.forest.builder.render;

import com.forest.level.block.Block;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Mathias on 31.05.16.
 */
public class BlockList extends JPanel {

    private List<Class<? super Block>> blockClasses = new ArrayList<>();    //Liste mit Blöcken
    private List<String> classNames;    //Blockname und Bilder
    private Map<String, ImageIcon> imageMap;
    private BuilderPanel builderPanel;
    private EditPanel editPanel;
    private FlagPanel flagPanel;
    private JList<String> jlist;

    public BlockList(BuilderPanel builderPanel) throws IOException, ClassNotFoundException {
        this.editPanel = new EditPanel();
        this.flagPanel = new FlagPanel(builderPanel);
        this.builderPanel = builderPanel;
        loadBlockClasses();
        this.classNames = loadClassNames();
        this.imageMap = createImageMap(classNames);

        jlist = new JList<>();
        jlist.setListData(classNames.toArray(new String[classNames.size()]));
        jlist.setCellRenderer(new ListRenderer(classNames));
        jlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                BlockList.this.selectedIndex(e.getFirstIndex());
            }
        });
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(jlist);
        this.add(flagPanel);
        this.add(editPanel);
        editPanel.setVisible(false);
        this.setBackground(Color.WHITE);
    }

    private void loadBlockClasses() throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String pack = "com.forest.level.block";
        Enumeration<URL> resources = classLoader.getResources(pack.replace(".", "/"));

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File dir = new File(url.getFile());
            if (dir.isDirectory()) {
                File[] fileList = dir.listFiles();
                if (fileList != null) {
                    for (File file : fileList) {
                        Class clazz = Class.forName(pack + "." + file.getName().substring(0, file.getName().length() - 6));
                        Class superClazz = clazz.getSuperclass();
                        if (superClazz != null) {
                            if (superClazz.equals(Block.class)) {
                                blockClasses.add((Class<? super Block>) clazz);
                            }
                        }
                    }
                }
            }
        }
    }

    private class ListRenderer extends DefaultListCellRenderer {

        private Font font = new Font("helvitica", Font.BOLD, 24);
        private Map<String, ImageIcon> imageMap;

        ListRenderer(List<String> classNames) throws IOException {
            imageMap = createImageMap(classNames);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon(imageMap.get((String) value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            return label;
        }
    }


    private Map<String, ImageIcon> createImageMap(List<String> list) throws IOException {
        Map<String, ImageIcon> map = new HashMap<>();
        for (String s : list) {
            URL res = com.forest.render.Renderer.class.getResource(s + ".png");
            if (res == null)
                res = com.forest.render.Renderer.class.getResource("GroundBlock.png");

            ImageIcon icon = new ImageIcon(res);

            icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            map.put(s, icon);
        }
        return map;
    }

    private List<String> loadClassNames() {
        List<String> names = new LinkedList<>();
        for (Class<? super Block> clazz : blockClasses) {
            names.add(clazz.getSimpleName());
        }
        return names;
    }

    /*
     * Method to fire when user selected block
     */
    private void selectedIndex(int index) {
        builderPanel.setBlockClassToCreate(blockClasses.get(index));
    }

    public void setBlockToEdit(Block lastSelected) {
        if (lastSelected != null) {
            editPanel.setBlock(lastSelected);
            editPanel.setVisible(true);
        } else {
            editPanel.setVisible(false);
        }
    }

    public void resetList() {
        jlist.clearSelection();
    }

    public void clearFlagList() {
        flagPanel.resetList();
    }
}
