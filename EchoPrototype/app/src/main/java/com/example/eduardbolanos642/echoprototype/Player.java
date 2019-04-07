package com.example.eduardbolanos642.echoprototype;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class Player
{
    private int[] position = new int[2];
    private int orientation;//0 North, 1 East, 2 South, 3 West
    //Map currentMap = getMap();

    Player()
    {
        position[0] = 0;
        position[1] = 0;
        orientation = 0;
    }

    public int[] getPosition()
    {
        return position;
    }

    public void turnLeft(Context context)
    {
        MediaPlayer turn;
        if(orientation == 0)
        {
            orientation = 3;
        }
        else
        {
            orientation--;
        }
        //play turn
        turn = MediaPlayer.create(context, R.raw.echolocate);
        turn.start();

    }

    public void turnRight(Context context)
    {
        MediaPlayer turn;
        if(orientation == 3)
        {
            orientation = 0;
        }
        else
        {
            orientation++;
        }
        //play turn
        turn = MediaPlayer.create(context, R.raw.echolocate);
        turn.start();
    }

    public int getOrientation()
    {
        return orientation;
    }

    public void attemptMoveForward(Context context) {
        MediaPlayer moveForward;
        int[] newPosition = new int[2];
        switch (orientation) {
            case 0:
                newPosition[0] = position[0];
                newPosition[1] = position[1] + 1;
            case 1:
                newPosition[0] = position[0] + 1;
                newPosition[1] = position[1];
            case 2:
                newPosition[0] = position[0];
                newPosition[1] = position[1] - 1;
            case 3:
                newPosition[0] = position[0] - 1;
                newPosition[1] = position[1];
    }
    if(iSLegal(newPosition))
    {
        position[0] = newPosition[0];
        position[1] = newPosition[1];
        //play footstep
        moveForward = MediaPlayer.create(context, R.raw.genericfootsteps);
        moveForward.start();
        //if tile is item get item, if tile is end play end
    }
    else {
        //play wall hit
        moveForward = MediaPlayer.create(context, R.raw.Wall_collision);
        moveForward.start();
        //if(Map.isLegal(newPosition)) == false  dont move forward play tileSound
    }
    }

    public void echolocate(Context context)
    {
        MediaPlayer echo;
        MediaPlayer collision;
        MediaPlayer leftTile;
        MediaPlayer rightTile;
        echo = MediaPlayer.create(context, R.raw.echolocate);

        int[] newPosition = new int[2];
        double volume = 100;//unknown what number
        //echo.setVolume(volume);
        switch (orientation) {
            case 0:
                newPosition[0] = position[0];
                newPosition[1] = position[1] + 1;
            case 1:
                newPosition[0] = position[0] + 1;
                newPosition[1] = position[1];
            case 2:
                newPosition[0] = position[0];
                newPosition[1] = position[1] - 1;
            case 3:
                newPosition[0] = position[0] - 1;
                newPosition[1] = position[1];
        }

        while(isLegal(newPosition))//is legal?
        {
            //play distance ping(tile sound)
            echo.start();
            //TODO decrement volume of ping each time
            //TODO add logic to play sound of object it passed
            //echo.setVolume(volume - 10);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            //if currentMap.TileatCoord(newPosition) == e;
            //MediaPlayer passing;
            //passing = MediaPlayer.create(context, R.raw.goalResponse);
            //            try {
            //                Thread.sleep(500);
            //            } catch (InterruptedException ex) {
            //                Thread.currentThread().interrupt();
            //            }
            switch (orientation) {
                case 0:
                    newPosition[1] = newPosition[1] + 1;
                case 1:
                    newPosition[0] = newPosition[0] + 1;
                case 2:
                    newPosition[1] = newPosition[1] - 1;
                case 3:
                    newPosition[0] = newPosition[0] - 1;
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //play wall
        collision = MediaPlayer.create(context, R.raw.Wall_collision);
        collision.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //go back to legal space
        switch (orientation) {
            case 0:
                newPosition[1] = newPosition[1] - 1;
            case 1:
                newPosition[0] = newPosition[0] - 1;
            case 2:
                newPosition[1] = newPosition[1] + 1;
            case 3:
                newPosition[0] = newPosition[0] + 1;
        }
        //play left
        switch (orientation) {
            case 0:
                newPosition[1] = newPosition[1] - 1;
            case 1:
                newPosition[0] = newPosition[0] + 1;
            case 2:
                newPosition[1] = newPosition[1] + 1;
            case 3:
                newPosition[0] = newPosition[0] - 1;
        }
        //char leftTile = currentMap.TileatCoord(newPosition);
        //if (leftTile == e);
        //leftSound = MediaPlayer.create(context, R.raw.goalResponse);
        //            try {
        //                Thread.sleep(500);
        //            } catch (InterruptedException ex) {
        //                Thread.currentThread().interrupt();
        //            }
        //else if leftTile == w);
        //leftSound = MediaPlayer.create(context, R.raw.Wall_Left);
        //left.start()
        //else if leftTile == f
        //leftSound = MediaPlayer.create(context, R.raw.Empty_space_left);
        //leftSound.start()
        //stop playing leftSound
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        //play right

        switch (orientation) {
            case 0:
                newPosition[1] = newPosition[1] + 1;
            case 1:
                newPosition[0] = newPosition[0] - 1;
            case 2:
                newPosition[1] = newPosition[1] - 1;
            case 3:
                newPosition[0] = newPosition[0] + 1;
        }
        //char rightTile = currentMap.TileatCoord(newPosition);
        //if (rightTile == e);
        //rightSound = MediaPlayer.create(context, R.raw.goalResponse);
        //            try {
        //                Thread.sleep(500);
        //            } catch (InterruptedException ex) {
        //                Thread.currentThread().interrupt();
        //            }
        //else if rightTile == w);
        //rightSound = MediaPlayer.create(context, R.raw.Wall_Right);
        //left.start()
        //else if rightTile == f
        //rightSound = MediaPlayer.create(context, R.raw.Empty_space_right);
        //rightSound.start()
        //stop playing rightSound

    }


}


