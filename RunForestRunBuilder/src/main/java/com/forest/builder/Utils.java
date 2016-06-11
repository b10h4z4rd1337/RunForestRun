package com.forest.builder;

import com.forest.level.LevelData;
import com.forest.level.block.Block;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * Created by Mathias on 31.05.16.
 */
public class Utils {

    private static byte[] levelDataToBytes(LevelData levelData) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(levelData);
        oos.flush();
        return bos.toByteArray();
    }

    private static String receiveMessage(InputStream is) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        String result = "";
        String line;
        while ((line = rd.readLine()) != null) {
            result += line;
        }

        return result;
    }

    public static boolean upload(LevelData levelData) throws Exception {
        byte[] data = levelDataToBytes(levelData);

        String urlString = "http://runforestrun-duerer.rhcloud.com/level";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(urlString);

        HttpEntity entity = MultipartEntityBuilder.create().addTextBody("option", "putLevel").addTextBody("name", "TestLevel")
                .addBinaryBody("data", data).build();

        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        String responseMessage = receiveMessage(response.getEntity().getContent());

        if (!responseMessage.equals("OK")) {
            throw new Exception("Something in Upload went wrong");
        }
        return true;
    }

    public static LevelData download() throws IOException, ClassNotFoundException, URISyntaxException {
        String urlString = "http://runforestrun-duerer.rhcloud.com/level";

        URIBuilder uriBuilder = new URIBuilder(urlString).addParameter("option", "getLevel").addParameter("name", "TestLevel");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uriBuilder.build());

        HttpResponse response = client.execute(request);

        InputStream is = response.getEntity().getContent();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 10];
        int read;
        while ((read = is.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, read);
        }

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return (LevelData) ois.readObject();
    }

    public static void levelDataToFile(LevelData levelData, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(levelData);
    }

    public static LevelData fileToLevelData(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (LevelData) ois.readObject();
    }

    private static JFileChooser createFileChooser() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isFile() && f.getAbsolutePath().endsWith(".rfr");
            }

            @Override
            public String getDescription() {
                return "RunForestRun Levels (.rfr)";
            }
        });
        return fileChooser;
    }

    public static File openFileOpenDialog(Component parent) {
        final JFileChooser fileChooser = createFileChooser();
        int retVal = fileChooser.showOpenDialog(parent);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static File openFileSaveDialog(Component parent) {
        final JFileChooser fileChooser = createFileChooser();
        int retVal = fileChooser.showSaveDialog(parent);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static Block createBlockFromClass(Class<? super Block> blockClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? super Block> cons = blockClass.getConstructor(int.class, int.class, String.class);
        return (Block) cons.newInstance(0, 300, blockClass.getSimpleName() + ".png");
    }

    public static void handleException(JFrame jframe, Exception e) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(jframe, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
    }
}
