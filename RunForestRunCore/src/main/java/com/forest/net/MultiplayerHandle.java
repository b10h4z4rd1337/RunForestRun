package com.forest.net;

/**
 * Created by Mathias on 25.06.2016.
 */
public interface MultiplayerHandle {
    void setServer(MultiplayerServer server);
    MultiplayerData getLastData();
    void send(String data);
    void close();
}
