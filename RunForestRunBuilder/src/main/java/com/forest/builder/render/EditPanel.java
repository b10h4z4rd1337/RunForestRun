package com.forest.builder.render;

import com.forest.level.block.Block;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;

/**
 * Created by Mathias on 07.06.16.
 */
public class EditPanel extends JPanel {

    private Block block;
    private JSpinner widthSpinner, heightSpinner;

    public EditPanel() {
        widthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        heightSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        addChangeListenerTo(widthSpinner, new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (block != null) {
                    int val = (int) widthSpinner.getValue();
                    int height = block.getBounds().height;
                    block.setSize(Block.BLOCK_SIDE * val, height);
                }
            }
        });
        addChangeListenerTo(heightSpinner, new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (block != null) {
                    int val = (int) heightSpinner.getValue();
                    int width = block.getBounds().width;
                    block.setSize(width, Block.BLOCK_SIDE * val);
                }
            }
        });
        this.setLayout(new GridLayout(2, 2));
        this.add(new JLabel("Width: "));
        this.add(widthSpinner);
        this.add(new JLabel("Height: "));
        this.add(heightSpinner);
    }

    private static void addChangeListenerTo(JSpinner spinner, ChangeListener changeListener) {
        JComponent comp = spinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        spinner.addChangeListener(changeListener);
    }

    public void setBlock(Block block) {
        this.block = block;
        com.forest.Rectangle bounds = block.getBounds();
        widthSpinner.setValue(bounds.width / 50);
        heightSpinner.setValue(bounds.height / 50);
    }

}
