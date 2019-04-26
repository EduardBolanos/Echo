package com.example.echo.echo_v_1_0_2;

import android.content.Context;
import android.os.VibrationEffect;

import java.util.ArrayList;

public class LevelManager {

    private static LevelManager sLevelManager;

    private ArrayList<String> mLevels;
    private ArrayList<Door> mDoors;
    private int mCurrentLevel;
    private int mAmbientSFX;

    private Tile mMapBuffer[][];
    private Tile mMap[][];
    private int mPlayerSpawnPosition[];
    private int mPlayerSpawnOrientation;
    private int mGoalPosition[];
    private Context context;
    protected Tile wall,floor,end,door;
    protected int sizeX, sizeY;

    private ArrayList<Item> mItemsToSpawn;

    private LevelManager(Context context)
    {
        mLevels = new ArrayList<String>();
        for(int x = 1; x < 20; x++){
            mLevels.add(("level" + x + ".txt"));
        }
        mCurrentLevel = 0;
        sizeX = 0;
        sizeY = 0;
        wall = new Tile('w');
        floor = new Tile('f');
        end = new Tile('e');
        door = new Tile('d');
        mItemsToSpawn = new ArrayList<Item>();
        mDoors = new ArrayList<Door>();

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
            case 'd':
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

    public boolean openDoor(int[] position){
        // checks and compare keys to door found at location
        Door theDoor = null;
        for(int i = 0; i < mDoors.size(); i++){
            theDoor = mDoors.get(i);
            if(position[0] == theDoor.mLocation[0] && position[1] == theDoor.mLocation[1]){
                break;
            }
        }
        for(int i = 0; i < mItemsToSpawn.size(); i++){
            if(theDoor.getPasscode().equals(mItemsToSpawn.get(i).getPassCode()) && mItemsToSpawn.get(i).getStatus() != 0){
                mMap[position[0]][position[1]].setType('f');
                mItemsToSpawn.get(i).setStatus(2);
                return true;
            }
        }
        return false;
    }

    public boolean hasKey(int[] position) {
        for(int i = 0; i < mItemsToSpawn.size(); i++) {
            if (position[0] == mItemsToSpawn.get(i).getLocation()[0] && position[1] == mItemsToSpawn.get(i).getLocation()[1]) {
                return true;
            }
        }
        return false;
    }
    public void addDoor(Door aDoor){
        mDoors.add(aDoor);
    }
    public void addItem(Item aItem){
        mItemsToSpawn.add(aItem);
    }

    public boolean pickUpKey(int[] position) {
        for(int i = 0; i < mItemsToSpawn.size(); i++) {
            if ((position[0] == mItemsToSpawn.get(i).getLocation()[0] && position[1] == mItemsToSpawn.get(i).getLocation()[1])) {
                if (mItemsToSpawn.get(i).getStatus() == 0) {
                    mItemsToSpawn.get(i).setStatus(1);
                    return true;
                } else {
                    return false;
                }
            }
        }
            return false;
    }

    public Item getItem(int index){
        return mItemsToSpawn.get(index);
    }
    public int getItemArraySize(){
        return mItemsToSpawn.size();
    }
    public Door getDoor(int index){
        return mDoors.get(index);
    }
    public int getDoorArraySize(){
        return mDoors.size();
    }
    public String getLevel(int cronologicalOrder){
        return mLevels.get(cronologicalOrder);
    }

    public Door getDoorAtPosition(int[] position){
        Door theDoor = null;
        for(int i = 0; i < mDoors.size(); i++){
            theDoor = mDoors.get(i);
            if(position[0] == theDoor.mLocation[0] && position[1] == theDoor.mLocation[1]){
                return theDoor;
            }
        }
        return null;
    }

    public Item getItemAtPosition(int[] position){
        Item theItem = null;
        for(int i = 0; i < mItemsToSpawn.size(); i++){
             theItem= mItemsToSpawn.get(i);
            if(position[0] == theItem.getLocation()[0] && position[1] == theItem.getLocation()[1]){
                return theItem;
            }
        }
        return null;
    }

    public void resetDoors() {
        mDoors = new ArrayList<Door>();
    }
}
