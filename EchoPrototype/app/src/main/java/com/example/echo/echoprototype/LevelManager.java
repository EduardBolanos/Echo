package com.example.echo.echoprototype;

import android.content.Context;
import android.os.VibrationEffect;

import java.util.ArrayList;

public class LevelManager {

    private static LevelManager sLevelManager;

    private ArrayList<String> mLevels;
    private int mCurrentLevel;
    private int mAmbientSFX;

    private Tile mMapBuffer[][];
    private Tile mMap[][];
    private int mPlayerSpawnPosition[];
    private int mPlayerSpawnOrientation;
    private int mGoalPosition[];
    private Context context;
    protected Tile wall,floor,end;

    private ArrayList<Item> mItemsToSpawn;

    private LevelManager(Context context)
    {
        mCurrentLevel = 0;
        wall = new Tile('w');
        floor = new Tile('f');
        end = new Tile('e');

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

    public Tile getTileAtCoord(int coord[])
    {
        return mMap[coord[0]][coord[1]];
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

    public void setMap(Tile map[][]){
        mMap = map;
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
