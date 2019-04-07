package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

public class Player
{
    private int mPosition[];
    private int mOrientation = 0;//0 North, 1 East, 2 South, 3 West
    private int mEchoSFX;
    private ArrayList<Item> mInventory;

    public int[] getmPosition()
    {
        return mPosition;
    }
    public int getOrientation()
    {
        return mOrientation;
    }

    public void turnLeft()
    {
        if(mOrientation == 0)
        {
            mOrientation = 3;
        }
        else
        {
            mOrientation--;
        }
    }

    public void turnRight()
    {
        if(mOrientation == 3)
        {
            mOrientation = 0;
        }
        else
        {
            mOrientation++;
        }
    }


    public void attemptMoveForward(Level level) {
        int newPosition[] = {0,0};

        switch (mOrientation) {
            case 0:
                newPosition[0] = mPosition[0];
                newPosition[1] = mPosition[1] + 1;
            case 1:
                newPosition[0] = mPosition[0] + 1;
                newPosition[1] = mPosition[1];
            case 2:
                newPosition[0] = mPosition[0];
                newPosition[1] = mPosition[1] - 1;
            case 3:
                newPosition[0] = mPosition[0] - 1;
                newPosition[1] = mPosition[1];
    }
        if(level.isLegal(newPosition))
        {
            mPosition[0] = newPosition[0];
            mPosition[1] = newPosition[1];
            //play footstep
        }
        else
        {
            //play wall hit
        }
        //if(Map.isLegal(newmPosition)) == false  dont move forward play tileSound
        //if true mPosition = newmPosition play footstep


    }

    public void echolocate()
    {
        //ki8ll mee
    }


}


