package com.example.echo.echoprototype;

import android.content.Context;
import android.os.VibrationEffect;

import java.util.ArrayList;

public class LevelManager {

    private static LevelManager sLevelManager;

    private ArrayList<String> mLevels;
    private int mCurrentLevel;
    private int mAmbientSFX;
    private int mAmbientSFXVolume;
    private int mFootstepSFXVolume;
    private int mNarrationVolume;

    private Tile mMapBuffer[][];
    private Tile mMap[][];
    private int mPlayerSpawnPosition[];
    private int mPlayerSpawnOrientation;
    private int mGoalPosition[];
    private Context context;
    private Tile wall,floor,end;

    private ArrayList<Item> mItemsToSpawn;

    private LevelManager(Context context)
    {
        mCurrentLevel = 0;
        mAmbientSFXVolume = 0;
        mFootstepSFXVolume = 0;
        mNarrationVolume = 0;
    }

    // generate singleton if it has not already been generated
    public static LevelManager get(Context context)
    {
        if (sLevelManager == null)
        {
            sLevelManager = new LevelManager(context);
        }
        return sLevelManager;
    }

    public ArrayList<String> getLevels() {
        return mLevels;
    }

    public void setLevels(ArrayList<String> levels) {
        mLevels = levels;
    }

    public void setCurrentLevel(int id)
    {
        mCurrentLevel = id;
    }

    public int getCurrentLevel()
    {
        return mCurrentLevel;
    }

    public void addLevel(String level)
    {
        mLevels.add(level);
    }

    public void setMapBuffer(Tile map[][])
    {
        mMapBuffer = map;
    }

    public Tile[][] getMapBuffer()
    {
        return mMapBuffer;
    }

    public Tile[][] getMap()
    {
        return mMap;
    }

    public void getMapFromBuffer()
    {
        mMap = mMapBuffer;
    }

    public void generateLevelFromConfigFile(String fileName)
    {

    }

    // this is called from generateLevelFromConfigFile
    public void generateTileFromConfigFile(String fileName)
    {

    }

    // this is called from generateLevelFromConfigFile
    public void generateItemFromConfigFile(String fileName)
    {
        String itemName;
        int itemId;
        VibrationEffect itemTactileId;
        int itemTactileIdNonVibrate;
        int itemAuditoryId;
    }

    public int getAmbientSFX() {
        return mAmbientSFX;
    }

    public void setAmbientSFX(int ambientSFX) {
        mAmbientSFX = ambientSFX;
    }

    public int[] getPlayerSpawnPosition() {
        return mPlayerSpawnPosition;
    }

    public void setPlayerSpawnPosition(int[] playerSpawnPosition) {
        mPlayerSpawnPosition = playerSpawnPosition;
    }

    public int getPlayerSpawnOrientation() {
        return mPlayerSpawnOrientation;
    }

    public void setPlayerSpawnOrientation(int playerSpawnOrientation) {
        mPlayerSpawnOrientation = playerSpawnOrientation;
    }

    public int[] getGoalPosition() {
        return mGoalPosition;
    }

    public void setGoalPosition(int[] goalPosition) {
        mGoalPosition = goalPosition;
    }

    public ArrayList<Item> getItemsToSpawn() {
        return mItemsToSpawn;
    }

    public void setItemsToSpawn(ArrayList<Item> itemsToSpawn) {
        mItemsToSpawn = itemsToSpawn;
    }
}
