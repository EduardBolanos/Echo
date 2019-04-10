package com.example.echo.echoprototype;

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

    public Level(int id, int[] endPoint)
    {
        mId = id;
        mMap = new Tile[10][10];
        Tile wall = new Tile('w');
        Tile floor = new Tile('f');
        Tile end = new Tile('e');

        mMap[endPoint[0]][endPoint[1]] = end;
        mMap[1][1] = floor;
        mMap[1][2] = floor;
        mMap[1][3] = floor;
        mMap[1][4] = floor;
        mMap[2][4] = floor;
        mMap[3][4] = floor;
        mMap[4][4] = floor;
        mMap[4][5] = floor;
        mMap[4][6] = floor;

        for(int x = 0; x < mMap.length; x++)
        {
            for(int y = 0; y < mMap[x].length; y++)
            {
                if(mMap[x][y] == null)
                {
                    mMap[x][y] = wall;
                }
            }
        }

    }

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
                return true;

                default:
                    return false;

        }
    }




}