package com.forest.android.net.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.forest.android.MainActivity;
import com.forest.net.MultiplayerProvider;
import com.forest.net.MultiplayerServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by b10h4 on 05.07.2016.
 */
public class AndroidBluetoothMultiplayerProvider extends MultiplayerProvider {

    public static final String UUID = "d075185a-6eb5-4d9b-97ad-7f1f6fed0e91";

    private BluetoothAdapter mBluetoothAdapter;
    private LinkedList<BluetoothDevice> foundDevices = new LinkedList<>();
    private HostFoundCallback callback;

    private BluetoothServerSocket serverSocket;
    private volatile AtomicBoolean continueRunning = new AtomicBoolean(true);

    private BluetoothSocket socket;

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                foundDevices.add(device);
                callback.hostFound(device.getName());
            }
        }
    };

    @Override
    public void startCollect(final MultiplayerServer.ClientFoundCallback callback) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                MainActivity.CONTEXT.startActivityForResult(enableBtIntent, 42);
            }

            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            MainActivity.CONTEXT.startActivity(discoverableIntent);

            try {
                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("RunForestRun", java.util.UUID.fromString(UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (continueRunning.get()) {
                        if (serverSocket != null) {
                            try {
                                BluetoothSocket socket = serverSocket.accept();
                                callback.clientFound(new AndroidBluetoothMultiplayerHandle(socket));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void endCollect() {
        continueRunning.set(false);
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endDiscover() {
        mBluetoothAdapter.cancelDiscovery();
        MainActivity.CONTEXT.unregisterReceiver(mReceiver);
    }

    @Override
    public void discover(final HostFoundCallback callback) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                MainActivity.CONTEXT.startActivityForResult(enableBtIntent, 42);
            }
            mBluetoothAdapter.startDiscovery();

            this.callback = callback;

            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            MainActivity.CONTEXT.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }
    }

    @Override
    public void connect(String host) {
        for (BluetoothDevice device : foundDevices) {
            if (host.equals(device.getName())) {
                try {
                    socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                    socket.connect();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        return "Bluetooth";
    }

}
