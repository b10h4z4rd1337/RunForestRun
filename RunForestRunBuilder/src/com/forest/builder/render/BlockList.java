package com.forest.builder.render;

import com.forest.level.block.Block;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mathias on 31.05.16.
 */
public class BlockList extends Component {

    private List<Class<? super Block>> blockClasses = new ArrayList<>();
    private List<String> classNames;

    public BlockList() throws IOException, ClassNotFoundException {
        loadBlockClasses();
        classNames = loadClassNames();
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
                        if (clazz.getSuperclass().equals(Block.class)) {
                            blockClasses.add((Class<? super Block>)clazz);
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

    private List<Image> loadClassImages() {
        //TODO: Implement class image loading
        return null;
    }

}
