package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

public class Level
{
    private int mId;
    private ArrayList<Item> mItemCoordPair;
    private Tile mMap[][];
    private int mAmbientSFX;
    private int mPlayerSpawnPoint[];
    private int mEndPoint[];


    public int getId()
    {
        return mId;
    }
    public void setId(int id)
    {
        mId=id;
    }

    public Tile getTileAtCoord(int coord[])
    {
        return mMap[coord[0]][coord[1]];
    }
    public int getPlayerSpawnPointX()
    {
        return mPlayerSpawnPoint[0];
    }
    public int getPlayerSpawnPointY()
    {
        return mPlayerSpawnPoint[1];
    }
    public void setPlayerSpawnPoint(int position[])
    {
        mPlayerSpawnPoint=position;
    }

    public void setAmbientSFX(int ambientSFX)
    {
        mAmbientSFX=ambientSFX;
    }
    public int getAmbientSFX()
    {
        return mAmbientSFX;
    }

    public void addUnspawnedItem(Item item, int position[])
    {

    }

    public boolean isLegal(int position[])
    {
        char location = mMap[position[0]][position[1]].getType();
        switch (location) {
            case 'w':
                return false;
            case 'f':
                return true;
            case 'e':
                //exit level progress to next
                return true;

                default:
                    return false;

        }
    }




}