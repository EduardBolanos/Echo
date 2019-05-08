package com.example.echo.echo_v_1_0_2;

import android.content.Context;

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

    public int[] moveFromPosition(int orientation, int[] position){
        int[] newPosition = new int[2];
        switch (orientation) {
            case 0:
                newPosition[0] = position[0];
                newPosition[1] = position[1] + 1;
                break;
            case 1:
                newPosition[0] = position[0] + 1;
                newPosition[1] = position[1];
                break;
            case 2:
                newPosition[0] = position[0];
                newPosition[1] = position[1] - 1;
                break;
            case 3:
                newPosition[0] = position[0] - 1;
                newPosition[1] = position[1];
                break;
        }
        return newPosition;
    }


}


