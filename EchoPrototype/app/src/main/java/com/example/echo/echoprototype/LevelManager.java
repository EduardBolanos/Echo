package com.example.echo.echoprototype;

import java.util.ArrayList;

public class LevelManager {

    private ArrayList<Level> mLevels;
    private int mCurrentLevel;
    private int mAmbientSFXVolume;
    private int mFootstepSFXVolume;

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
        // map configuration, etc.
    }


}
