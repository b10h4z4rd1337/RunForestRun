package com.forest.net;

/**
 * Created by user on 24.06.2016.
 */
public abstract class MultiplayerProvider {

    public static MultiplayerProvider[] availableProviders = new MultiplayerProvider[1];

    public abstract void startCollect(MultiplayerServer.ClientFoundCallback callback);
    public abstract void endCollect();

    public abstract void endDiscover();
    public abstract void discover(HostFoundCallback callback);
    public abstract void connect(String host);
    public abstract String receive();
    public abstract void send(String data);

    public abstract String getName();

    public interface HostFoundCallback {
        void hostFound(String host);
    }

}
