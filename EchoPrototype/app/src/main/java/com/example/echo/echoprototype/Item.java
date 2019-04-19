package com.example.echo.echoprototype;

import android.os.VibrationEffect;

public class Item {

    private String mName;
    private int mId;
    private VibrationEffect mTactileId;
    private int mTactileIdNonVibrate;
    private int mAuditoryId;

        public Item(int id, String name){
            mId = id;
            mName = name;
        }

        public int getId() {return mId;}
        public void setId(int id) {mId = id;}
        public VibrationEffect getTactileId(){
            return mTactileId;
        }
        public void setTactileId(VibrationEffect tactileId) {mTactileId = tactileId;}
        public int getTactileIdNonVibrate() {return mTactileIdNonVibrate;}
        public void setTactileIdNonVibrate(int tactileIdNonVibrate) {mTactileIdNonVibrate = tactileIdNonVibrate;}
        public String getName(){
            return mName;
        }
        public void setName (String name) {mName = name;}
        public int getAuditoryId(){
            return mAuditoryId;
        }
        public void setAuditoryId(int auditoryId) {mAuditoryId = auditoryId;}
}

