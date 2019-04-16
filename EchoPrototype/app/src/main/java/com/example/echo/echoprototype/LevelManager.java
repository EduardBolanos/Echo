package com.example.echo.echoprototype;

import android.content.Context;

import java.util.ArrayList;

public class LevelManager {

    private static LevelManager sLevelManager;

    private ArrayList<Level> mLevels;
    private int mCurrentLevel;
    private int mAmbientSFXVolume;
    private int mFootstepSFXVolume;
    private int mNarrationVolume;

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

    public ArrayList<Level> getLevels() {
        return mLevels;
    }

    public void setLevels(ArrayList<Level> levels) {
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

    public void addLevel(Level level)
    {
        mLevels.add(level);
    }

    public void initializeLevel()
    {
        // contains important initialization logic, including item spawning, player spawn configuration,
        // map configuration, etc
    }


}
