package com.example.echo.echoprototype;

import android.content.Context;
import android.media.MediaPlayer;

public class Player
{
    private int[] position = new int[2];
    private int orientation;//0 North, 1 East, 2 South, 3 West
    Context mContext;
    public Player(Context context)
    {
        mContext = context;
    }
    public void setOrientation(int ori){
        orientation = ori;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int[] getPosition()
    {
        return position;
    }

    public int getOrientation()
    {
        return orientation;
    }


}


