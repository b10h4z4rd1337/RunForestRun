package com.forest.builder.render;

import javax.swing.*;

/**
 * Created by user on 08.06.2016.
 */
public class FlagPanel extends JPanel {

    private static final String[] flags = new String[] {"START", "END"};

    private BuilderPanel builderPanel;
    private JList<String> jlist;

    public FlagPanel(BuilderPanel builderPanel) {
        this.builderPanel = builderPanel;

        jlist = new JList<>();
        jlist.setListData(flags);
        jlist.addListSelectionListener(e -> {
            if (jlist.getSelectedIndex() >= 0) {
                builderPanel.setFlag(flags[jlist.getSelectedIndex()]);
            }
        });
        this.add(jlist);
    }

    public void resetList() {
        jlist.clearSelection();
    }

}
