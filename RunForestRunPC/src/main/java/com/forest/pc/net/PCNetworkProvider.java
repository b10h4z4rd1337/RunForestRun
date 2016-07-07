package com.forest.pc.net;

import com.forest.net.MultiplayerProvider;
import com.forest.net.MultiplayerServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Mathias on 25.06.2016.
 */
public class PCNetworkProvider extends MultiplayerProvider {

    public static final String BROADCAST_MESSAGE = "Forest, Im here!";
    public static final byte[] BROADCAST_MESSAGE_BYTES = BROADCAST_MESSAGE.getBytes();

    private DatagramSocket datagramSocket;
    private ServerSocket ss;
    private Socket socket;
    private volatile AtomicBoolean continueRunning = new AtomicBoolean(true);

    @Override
    public void startCollect(final MultiplayerServer.ClientFoundCallback callback) {

        new Thread(() -> {

            try {
                String hostAddress = "192.168.178.21";
                String broadcastAddress = hostAddress.substring(0, hostAddress.lastIndexOf(".")) + ".255";

                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(BROADCAST_MESSAGE_BYTES, 0, BROADCAST_MESSAGE_BYTES.length,
                        InetAddress.getByName(broadcastAddress), 1337 * 2);

                while (continueRunning.get()) {
                    socket.send(packet);
                    Thread.sleep(1000);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        try {
            ss = new ServerSocket(1337);

            new Thread(() -> {
                try {
                    while (continueRunning.get()) {
                        Socket socket = ss.accept();
                        callback.clientFound(new PCMultiplayerHandle(socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endCollect() {
        continueRunning.set(false);
        try {
            ss.close();
        } catch (IOException ignored) { }
    }

    @Override
    public void discover(HostFoundCallback callback) {
        new Thread(() -> {

            try {
                datagramSocket = new DatagramSocket(1337 * 2);

                while (continueRunning.get()) {
                    byte[] data = new byte[BROADCAST_MESSAGE_BYTES.length];
                    DatagramPacket packet = new DatagramPacket(data, 0, BROADCAST_MESSAGE_BYTES.length);
                    datagramSocket.receive(packet);
                    String received = new String(data, 0, packet.getLength()).trim();
                    if (received.equals(BROADCAST_MESSAGE)) {
                        callback.hostFound(packet.getAddress().getHostAddress());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    @Override
    public void endDiscover() {
        continueRunning.set(false);
        datagramSocket.close();
    }

    @Override
    public void connect(String host) {
        try {
            socket = new Socket(InetAddress.getByName(host), 1337);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String receive() {
        String res = null;
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[512];
            int read;

            read = inputStream.read(buffer, 0, 512);
            if (read != -1)
                res = new String(buffer, 0, read);
            else
                res = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public void send(String data) {
        if (socket != null) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(data.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "LAN";
    }

}
