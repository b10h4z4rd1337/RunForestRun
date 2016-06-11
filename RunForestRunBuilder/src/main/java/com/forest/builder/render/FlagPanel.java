package com.forest.builder.render;

import javax.swing.*;

/**
 * Created by user on 08.06.2016.
 */
class FlagPanel extends JPanel {

    private static final String[] flags = new String[] {"START", "END"};

    private JList<String> jlist;

    FlagPanel(final BuilderPanel builderPanel) {
        jlist = new JList<>();
        jlist.setListData(flags);
        jlist.addListSelectionListener(e -> {
            if (jlist.getSelectedIndex() >= 0) {
                builderPanel.setFlag(flags[jlist.getSelectedIndex()]);
            }
        });
        this.add(jlist);
    }

    void resetList() {
        jlist.clearSelection();
    }

}
