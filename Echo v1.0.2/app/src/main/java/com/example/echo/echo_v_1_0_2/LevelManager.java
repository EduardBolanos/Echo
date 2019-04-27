package com.example.echo.echo_v_1_0_2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.VibrationEffect;

import java.io.FileInputStream;
import java.io.InputStream;
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
    public static LevelManager setNewLevelManager(Context context){
        return (new LevelManager(context));
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

    public void setCurrentLevel(int id)
    {
        mCurrentLevel = id;
    }

    public int getCurrentLevel()
    {
        return mCurrentLevel;
    }

    public void setMap(Tile map[][]){
        mMap = map;
    }

    public Tile[][] getMap()
    {
        return mMap;
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


    public boolean openDoor(int[] position){
        // checks and compare keys to door found at location
        Door theDoor = null;
        for(int i = 0; i < mDoors.size(); i++){
            theDoor = mDoors.get(i);
            if(position[0] == theDoor.getmLocation()[0] && position[1] == theDoor.getmLocation()[1]){
                break;
            }
        }
        for(int i = 0; i < mItemsToSpawn.size(); i++){
            if(theDoor.getPasscode().equals(mItemsToSpawn.get(i).getPassCode()) && mItemsToSpawn.get(i).getStatus() == 1){
                mMap[position[0]][position[1]].setType('f');
                mItemsToSpawn.get(i).setStatus(2);
                return true;
            }
        }
        return false;
    }

    public boolean hasKey(int[] position) {
        for(int i = 0; i < mItemsToSpawn.size(); i++) {
            if (position[0] == mItemsToSpawn.get(i).getLocation()[0] && position[1] == mItemsToSpawn.get(i).getLocation()[1] &&
                    mItemsToSpawn.get(i).getStatus() == 0) {
                return true;
            }
        }
        return false;
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
            if(position[0] == theDoor.getmLocation()[0] && position[1] == theDoor.getmLocation()[1]){
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

    public void nullLevelManager() {
        mLevels = null;
        mDoors = null;
        mMapBuffer = null;
        mMap = null;
        mPlayerSpawnPosition = null;
        mGoalPosition = null;
        wall = null;
        floor = null;
        end = null;
        door = null;
    }

    /*******************************************************************************************************************************************************
     *Don't look down here, the internals of the code. It is a working parser, don't worry about it.
     */

    public String loadLevel(InputStream asset, FileInputStream assetTwo, boolean saveGameStatus) {
        int data = 0;
        String concatinator = "";
        String narrator = null;
        int[] playerPos = new int[2];
        int state = 0;
        int locX = 0;
        int locY = 0;
        int bypass = 0;
        ArrayList<Item> refer = new ArrayList<Item>();
        ArrayList<Door> someDoors = new ArrayList<Door>();
        int keyLoc;
        Tile map[][] = null;
        ArrayList<Integer> passCodes = new ArrayList<Integer>();
        if (asset != null || assetTwo != null) {
            while (data != -1) {
                if(!saveGameStatus) {
                    try {
                        data = asset.read();
                    } catch (java.io.IOException e) {
                    }
                }else{
                    try {
                        data = assetTwo.read();
                    } catch (java.io.IOException e) {
                    }
                }
                if (((char) data != '\n' && ((char) data > 31))) {
                    switch (state) {
                        case 0:
                            if (data != 35) {
                                concatinator = concatinator + ((char) data);
                            } else {
                                setCurrentLevel(Integer.parseInt(concatinator));
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
                                map = new Tile[sizeX][sizeY];
                                locY = sizeY - 1;
                                concatinator = "";
                                state = 2;
                            }
                            break;
                        case 2:
                            if (data != 35) {
                                switch (data) {
                                    case 'f': // a walkable floor
                                        map[locX][locY] = floor;
                                        break;
                                    case 'e': // the goal
                                        map[locX][locY] = end;
                                        int[] ep = {locX, locY};
                                        setPlayerSpawnPosition(ep);
                                        break;
                                    case 'w': // adds a wall
                                        map[locX][locY] = wall;
                                        break;
                                    case 'P': // initiates player spawn
                                        map[locX][locY] = floor;
                                        playerPos[0] = locX; playerPos[1] = locY;
                                        setPlayerSpawnPosition(playerPos);
                                        break;
                                    case 'd': // adds a door
                                        map[locX][locY] = door;
                                        while (data != ')') {
                                            if (!saveGameStatus) {
                                                try {
                                                    data = asset.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            } else {
                                                try {
                                                    data = assetTwo.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            }
                                            if (data != '(' && data != ')') {
                                                concatinator = concatinator + ((char) data);
                                            } else if (data != '(') {
                                                int[] doorPos = {locX, locY};
                                                StringBuilder doorCode = new StringBuilder();
                                                doorCode.append(concatinator);
                                                Door aDoor = new Door(doorPos, doorCode.toString());
                                                someDoors.add(aDoor);
                                                concatinator = "";
                                            }
                                        }
                                        break;
                                    case 'k':
                                        map[locX][locY] = floor;
                                        while (data != ')') {
                                            if (!saveGameStatus) {
                                                try {
                                                    data = asset.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            } else {
                                                try {
                                                    data = assetTwo.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            }
                                            if (data != '(' && data != ')') {
                                                concatinator = concatinator + ((char) data);
                                            } else if (data != '(') {
                                                if(data == 'P'){
                                                    map[locX][locY] = floor;
                                                    playerPos[0] = locX; playerPos[1] = locY;
                                                    setPlayerSpawnPosition(playerPos);
                                                }
                                                int[] keyPos = {locX, locY};
                                                StringBuilder keyCode = new StringBuilder();
                                                keyCode.append(concatinator);
                                                Item aKey = new Item(keyCode.toString(), keyPos, 0); // 0 is key
                                                refer.add(aKey);
                                                concatinator = "";
                                            }
                                        }
                                        break;

                                }
                                locX++;
                            } else {
                                locY--;
                                locX = 0;
                            }
                            if (locY < 0) {
                                setMap(map);
                                state = 3;
                            }
                            break;
                        case 3:
                            concatinator = "";
                            concatinator = concatinator + ((char) data);
                            setPlayerSpawnOrientation(Integer.parseInt(concatinator));
                            concatinator = "";
                            if (!saveGameStatus) {// plays a new level intro
                                try {
                                    data = asset.read();
                                } catch (java.io.IOException e) {
                                }
                                while (data != -1) {
                                    try {
                                        data = asset.read();
                                    } catch (java.io.IOException e) {
                                    }
                                    if (((char) data != '\n' && ((char) data > 31))) {
                                        if (data != 35) {
                                            concatinator = concatinator + ((char) data);
                                        } else {
                                            narrator = concatinator;
                                            concatinator = "";
                                            data = -1;
                                        }
                                    }
                                }
                            }
                            if (refer.size() > 0) {
                                if (saveGameStatus) {
                                    try {
                                        data = assetTwo.read();
                                    } catch (java.io.IOException e) {
                                    }
                                }
                            }
                            concatinator = "";
                            state = 0;
                            keyLoc = 0;
                            int end = 1;
                            for (int x = 0; x < refer.size(); x++) { // adding items
                                end = 1;
                                while (end == 1) {
                                    if (!saveGameStatus) {
                                        try {
                                            data = asset.read();
                                        } catch (java.io.IOException e) {
                                        }
                                    } else {
                                        try {
                                            data = assetTwo.read();
                                        } catch (java.io.IOException e) {
                                        }
                                    }
                                    if (((char) data != '\n' && ((char) data > 31))) {
                                        if (data != 35) {
                                            concatinator = concatinator + ((char) data);
                                        } else {
                                            switch (state) {
                                                case 0:
                                                    for (int y = 0; y < refer.size(); y++) {
                                                        keyLoc = y;
                                                        if(refer.get(y).getPassCode().equals(concatinator)){
                                                            break;
                                                        }
                                                    }
                                                    concatinator = "";
                                                    state = 1;
                                                    break;
                                                case 1:
                                                    refer.get(keyLoc).setName(concatinator);
                                                    concatinator = "";
                                                    state = 2;
                                                    break;
                                                case 2:
                                                    refer.get(keyLoc).setAuditoryId(concatinator);
                                                    concatinator = "";
                                                    state = 3;
                                                    break;
                                                case 3:
                                                    concatinator = "";
                                                    state = 4;
                                                    break;
                                                case 4:
                                                    refer.get(keyLoc).setStatus(Integer.parseInt(concatinator));
                                                    concatinator = "";
                                                    state = 0;
                                                    keyLoc = 0;
                                                    end = 0;
                                            }
                                        }
                                    }
                                }
                            }
                            mItemsToSpawn = refer;
                            mDoors = someDoors;
                            return narrator;
                    }
                }
            }
        }
        return null;
    }

}
