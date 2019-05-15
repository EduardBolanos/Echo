package com.example.echo.echo_v_1_0_2;

import android.os.VibrationEffect;

public class Tile
{
    private char mType;
    private Item mItem;
    private VibrationEffect mTactileId;
    private int mAuditoryId;
    private int mFootstepSFX;

    public Tile(char type)
    {
        mType = type;
    }

    public Item getItem() {
        return mItem;
    }

    public void setItem(Item item) {
        mItem = item;
    }

    public VibrationEffect getTactileId() {
        return mTactileId;
    }

    public void setTactileId(VibrationEffect tactileId) {
        mTactileId = tactileId;
    }

    public int getAuditoryId() {
        return mAuditoryId;
    }

    public void setAuditoryId(int auditoryId) {
        mAuditoryId = auditoryId;
    }

    public char getType()
    {
        return mType;
    }
    public void setType(char type)
    {
        mType = type;
    }

    public int getFootstepSFX() {
        return mFootstepSFX;
    }

    public void setFootstepSFX(int footstepSFX) {
        mFootstepSFX = footstepSFX;
    }


}