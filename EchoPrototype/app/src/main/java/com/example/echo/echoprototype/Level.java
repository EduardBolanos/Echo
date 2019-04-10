package com.example.echo.echoprototype;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class Level
{
    private int mId;
    private ArrayList<Item> mItemCoordPair;
    private Tile mMap[][];
    private int mAmbientSFX;
    private int mPlayerSpawnPoint[];
    private int mStartOrientation;
    private int mEndPoint[];
    private Context context;
    private Tile wall,floor,end;
    public Level(Context that)
    {
        wall = new Tile('w');
        floor = new Tile('f');
        end = new Tile('e');
        context = that;

    }
    public boolean loadLevel(String fileName){
        InputStream asset;
        int data = 0;
        String concatinator = "";
        int state = 0;
        int sizeX = 0;
        int sizeY = 0;
        int locX = 0;
        int locY = 0;
        int bypass = 0;
        try{
            asset = context.getAssets().open(fileName);
            // add tiles
            // add id
            // add end
            // set spawn
        }catch (java.io.IOException e){
            return false;
        }
        while(data != -1) {
            try {
                data = asset.read();
            }catch(java.io.IOException e){
            }
            if((char)data != '\n' && ((char)data > 31 || data == 13)) {
                switch (state) {
                    case 0:
                        if (data != 13) {
                            concatinator = concatinator + ((char) data);
                        } else {
                            mId = Integer.parseInt(concatinator);
                            concatinator = "";
                            state = 1;
                        }
                        break;
                    case 1:
                        if (data != 13) {
                            if (bypass == 1) {
                                concatinator = concatinator + ((char) data);
                            }
                            else {
                                if (data != 32 && bypass == 0) {
                                    concatinator = concatinator + ((char) data);
                                } else {
                                    sizeX = Integer.parseInt(concatinator);
                                    concatinator = "";
                                    bypass = 1;
                                }
                            }
                        } else {
                            sizeY = Integer.parseInt(concatinator);
                            mMap = new Tile[sizeX][sizeY];
                            concatinator = "";
                            state = 2;
                        }
                        break;
                    case 2:
                        if (data != 13) {
                            switch (data) {
                                case 102:
                                    mMap[locX][locY] = floor;
                                    break;
                                case 101:
                                    mMap[locX][locY] = end;
                                    int[] ep = {locX, locY};
                                    mEndPoint = ep;
                                    break;
                                case 119:
                                    mMap[locX][locY] = wall;
                                    break;
                                case 50:
                                    mMap[locX][locY] = floor;
                                    int[] playerPos = {locX, locY};
                                    setPlayerSpawnPoint(playerPos);
                                    break;
                            }
                            locX++;
                        } else {
                            locY++;
                            locX = 0;
                        }
                        if (sizeY == locY) {
                            state = 3;
                        }
                        break;
                    case 3:
                        concatinator = "";
                        concatinator = concatinator + ((char) data);
                        mStartOrientation = Integer.parseInt(concatinator);
                        asset = null;
                        Runtime r = Runtime.getRuntime();
                        r.gc();
                        data = -1;

                        break;
                }
            }
        }

        return true;
    }
    public int getStartOrientation(){
        return mStartOrientation;
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