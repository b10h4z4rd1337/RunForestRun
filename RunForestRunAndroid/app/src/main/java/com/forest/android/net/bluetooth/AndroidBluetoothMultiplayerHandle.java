package com.forest.android.net.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.forest.net.MultiplayerData;
import com.forest.net.MultiplayerHandle;
import com.forest.net.MultiplayerPacket;
import com.forest.net.MultiplayerServer;
import com.forest.render.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by b10h4 on 05.07.2016.
 */
public class AndroidBluetoothMultiplayerHandle implements MultiplayerHandle {

    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final AtomicReference<MultiplayerData> lastPacket = new AtomicReference<>(new MultiplayerData());
    private MultiplayerServer server;

    public AndroidBluetoothMultiplayerHandle(BluetoothSocket socket) {
        this.socket = socket;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String res = "";
                        byte[] buffer = new byte[512];
                        int read;

                        while (!res.endsWith("|")) {
                            read = inputStream.read(buffer, 0, 512);
                            if (read != -1)
                                res += new String(buffer, 0, read);
                        }

                        if (res.endsWith("|")) {
                            String[] packets = res.split("\\|");
                            for (String packet : packets) {
                                if (!packet.isEmpty()) {
                                    MultiplayerData data = lastPacket.get();
                                    MultiplayerPacket multiplayerPacket = new MultiplayerPacket(packet);
                                    if (multiplayerPacket.getOption().equals(MultiplayerPacket.REMOVE)) {
                                        server.removeBlock(multiplayerPacket.getBlockID());
                                    } else if (multiplayerPacket.getOption().equals(MultiplayerPacket.END)) {
                                        MultiplayerData mpData = lastPacket.get();
                                        mpData.time = multiplayerPacket.getTime();
                                        lastPacket.set(mpData);
                                    } else {
                                        data.lastPacket = multiplayerPacket;
                                        if (data.lastPacket.getOption().equals(MultiplayerPacket.COLOR))
                                            data.color = Color.fromString(data.lastPacket.getData());
                                        lastPacket.set(data);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public MultiplayerData getLastData() {
        return lastPacket.get();
    }

    @Override
    public void send(String data) {
        byte[] buffer = data.getBytes();
        try {
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setServer(MultiplayerServer server) {
        this.server = server;
    }
}
