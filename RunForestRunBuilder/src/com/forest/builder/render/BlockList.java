package com.forest.builder.render;

import com.forest.level.block.Block;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mathias on 31.05.16.
 * TODO: @Flo Implement GUI (eg. http://stackoverflow.com/questions/22266506/how-to-add-image-in-jlist)
 */
public class BlockList extends Component {

    private List<Class<? super Block>> blockClasses = new ArrayList<>();
    private List<String> classNames, imageNames;
    private BuilderPanel builderPanel;

    public BlockList(BuilderPanel builderPanel) throws IOException, ClassNotFoundException {
        loadBlockClasses();
        classNames = loadClassNames();
        this.builderPanel = builderPanel;
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

    private List<String> loadClassNames() {
        List<String> names = new LinkedList<>();
        for (Class<? super Block> clazz : blockClasses) {
            names.add(clazz.getSimpleName());
        }
        return names;
    }

    private List<String> loadClassImages() {
        //TODO: Implement class image loading
        return null;
    }

    /*
     * Method to fire when user selected block
     */
    private void selectedIndex(int index) {
        try {
            Class<? super Block> blockClass = blockClasses.get(index);
            Constructor<? super Block> cons = blockClass.getConstructor(int.class, int.class, int.class, int.class, String.class);
            Block result = (Block) cons.newInstance(0, 0, 0, 0, imageNames.get(index));
            builderPanel.setBlockToCreate(result);
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
