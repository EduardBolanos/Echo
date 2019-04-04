package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class Player
{
    int[2] position = {0,0};
    int orientation = 0;//0 North, 1 East, 2 South, 3 West

    public int[] getPosition()
    {
        return position;
    }

    public void turnLeft()
    {
        if(orientation == 0)
        {
            orienation = 3;
        }
        else
        {
            orientation--;
        }
    }

    public void turnRight()
    {
        if(orientation == 3)
        {
            orienation = 0;
        }
        else
        {
            orientation++;
        }
    }

    public int getOrientation()
    {
        return orientation;
    }
    public void attemptMoveForward() {
        int[2]newPosition;
        switch (orientation) {
            case 0:
                newPosition[0] = position[0];
                newposition[1] = position[1] + 1;
            case 1:
                newPosition[0] = position[0] + 1;
                newposition[1] = position[1];
            case 2:
                newPosition[0] = position[0];
                newposition[1] = position[1] - 1;
            case 3:
                newPosition[0] = position[0] - 1;
                newposition[1] = position[1];
    }
    if(iSLegal(newpPosition))
    {
        position[0] = newPosition[0];
        position[1] = newPosition[1];
        //play footstep
    }
    else
    {
        //play wall hit
    }
        //if(Map.isLegal(newPosition)) == false  dont move forward play tileSound
        //if true position = newPosition play footstep


    }

    public void echolocate()
    {
        //ki8ll mee
    }


}


