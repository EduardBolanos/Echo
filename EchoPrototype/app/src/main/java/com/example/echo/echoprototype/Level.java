package com.example.echo.echoprototype;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
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
    private InputStream asset;
    private Tile wall,floor,end;
    int sizeX;
    int sizeY;
    public Level(Context that)
    {
        wall = new Tile('w');
        floor = new Tile('f');
        end = new Tile('e');
        context = that;

    }
    public boolean loadLevel(String fileName, InputStream save){ // save determines if in asset or file
        int data = 0;
        String concatinator = "";
        int state = 0;
        int locX = 0;
        int locY = 0;
        int bypass = 0;
        if(save == null) {
            try {
                asset = context.getAssets().open(fileName);
                // add tiles
                // add id
                // add end
                // set spawn
            } catch (java.io.IOException e) {
                return false;
            }
        }
            while (data != -1) {
                if(save == null) {
                    try {
                        data = asset.read();
                    } catch (java.io.IOException e) {
                    }
                }
                  else if(save != null){
                    try {
                        data = save.read();
                    } catch (java.io.IOException e) {
                    }
                }
                if (((char) data != '\n' && ((char) data > 31))) {
                    switch (state) {
                        case 0:
                            if (data != 35) {
                                concatinator = concatinator + ((char) data);
                            } else {
                                mId = Integer.parseInt(concatinator);
                                concatinator = "";
                                state = 1;
                            }
                            break;
                        case 1:
                            if (data != 35) {
                                if (bypass == 1) {
                                    concatinator = concatinator + ((char) data);
                                } else {
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
                                locY = sizeY - 1;
                                concatinator = "";
                                state = 2;
                            }
                            break;
                        case 2:
                            if (data != 35) {
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
                                    case 80:
                                        mMap[locX][locY] = floor;
                                        int[] playerPos = {locX, locY};
                                        setPlayerSpawnPoint(playerPos);
                                        break;
                                }
                                locX++;
                            } else {
                                locY--;
                                locX = 0;
                            }
                            if (locY < 0) {
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
    public void saveGame(File saveFile){
        try {
            FileWriter fileWriter = new FileWriter(saveFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf(mId + "#" + sizeX + " " + sizeY + "#");
            for(int x = 0; x < sizeX; x++){
                for(int y = 0; y < sizeY; y++){
                    if(mEndPoint[0] == x && mEndPoint[1] == y){
                        printWriter.append('e');
                    }
                    else if(mPlayerSpawnPoint[0] == x && mPlayerSpawnPoint[1] == y){
                        printWriter.append('P');
                    }
                    else {
                        printWriter.append(mMap[x][y].getType());
                    }
                }
                printWriter.printf("#");
            }
            printWriter.printf("0#"); // STUB, when hooking to level manager, please use player method to get orientation
            printWriter.close();
            fileWriter.close();
        }
        catch(java.io.IOException e){
        }
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
    public int[] getPlayerSpawnPoint(){
        return mPlayerSpawnPoint;
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
                default:
                    return true;

        }
    }




}