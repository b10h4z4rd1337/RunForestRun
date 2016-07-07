package com.forest.net;

import com.forest.render.Color;

/**
 * Created by user on 01.07.2016.
 */
public class MultiplayerData {

    public long time;
    public Color color = Color.WHITE;
    public int index = -1;
    public MultiplayerHandle handle = null;
    public MultiplayerPacket lastPacket = null;

}
