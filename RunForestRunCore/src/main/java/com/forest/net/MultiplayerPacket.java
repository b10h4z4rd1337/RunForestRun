package com.forest.net;

/**
 * Created by user on 24.06.2016.
 */
public class MultiplayerPacket {

    public static final String START = "START", POS = "POS", END = "END", INDEX = "INDEX",
            LEVEL = "LEVEL", TIME = "TIME", GO = "GO", COLOR = "COLOR", REMOVE = "REMOVE";
    private String option;
    private String data;

    private float x, y;
    private long time = 0L;
    private int index = -1;
    private int blockID = -1;

    public MultiplayerPacket() { }

    public MultiplayerPacket(String toParse) {
        if (toParse.startsWith(START)) {
            option = START;
        } else if (toParse.startsWith(GO)) {
            option = GO;
        } else if (toParse.startsWith(INDEX)) {
            option = INDEX;
            String data = extractData(toParse, option);
            index = Integer.parseInt(data);
        } else if (toParse.startsWith(LEVEL)) {
            option = LEVEL;
            this.data = extractData(toParse, option);
        } else if (toParse.startsWith(TIME)) {
            option = TIME;
            String data = extractData(toParse, option);
            time = Long.parseLong(data);
        } else if (toParse.startsWith(REMOVE)) {
            option = REMOVE;
            String data = extractData(toParse, option);
            blockID = Integer.parseInt(data);
        } else {
            String[] tmp = toParse.split(":");
            this.index = Integer.parseInt(tmp[0]);
            toParse = tmp[1];
            if (toParse.startsWith(POS)) {
                option = POS;
                String data = extractData(toParse, option);
                String[] values = data.split(",");
                x = Float.parseFloat(values[0]);
                y = Float.parseFloat(values[1]);
            } else if (toParse.startsWith(END)) {
                option = END;
                String data = extractData(toParse, option);
                time = Long.parseLong(data);
            } else if (toParse.startsWith(COLOR)) {
                option = COLOR;
                data = extractData(toParse, option);
            }
        }
    }

    private String extractData(String parse, String option) {
        return parse.substring(option.length()).replace("{", "").replace("}", "");
    }

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public long getTime() {
        return time;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public String constructPacket() {
        if (index == -1 || option.equals(INDEX)) {
            return wrap(option + getDataString());
        } else {
            return wrap(index + ":" + option + getDataString());
        }
    }

    private String getDataString() {
        if (option.equals(POS)) {
            return "{" + x + "," + y + "}";
        } else if (option.equals(END) || option.equals(TIME)) {
            return "{" + time + "}";
        } else if (option.equals(LEVEL) || option.equals(COLOR)) {
            return "{" + data + "}";
        } else if (option.equals(INDEX)) {
            return "{" + index + "}";
        } else if (option.equals(LEVEL)) {
            return "{" + time + "}";
        } else if (option.equals(REMOVE)) {
            return "{" + blockID + "}";
        } else {
            return "";
        }
    }

    private String wrap(String data) {
        return "|" + data + "|";
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }
}
