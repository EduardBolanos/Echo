package com.example.echo.echo_v_1_0_2;

import android.os.VibrationEffect;

public class Item {

    private String mName;
    private VibrationEffect mTactileId;
    private int mTactileIdNonVibrate;
    private String mAuditoryId;
    private String mPassCode;
    private int mStatus; // 0 is ground, 1 is picked up.
    private int mType; // 0 is key
    private int[] mLocation;

        public Item(String passCode, int[] location, int type){
            mPassCode = passCode;
            mLocation = location;
            mType = type;
        }
        public int gettype(){ return mType;}
        public void settype(int type){ mType = type;}
        public int[] getLocation(){return mLocation;}
        public void setLocation(int[] location){mLocation = location;}
        public int getStatus(){ return mStatus;}
        public void setStatus(int status){mStatus = status;}
        public String getPassCode() {return mPassCode;}
        public void setPassCode(String passCode) {mPassCode = passCode;}
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
        public String getAuditoryId(){
            return mAuditoryId;
        }
        public void setAuditoryId(String auditoryId) {mAuditoryId = auditoryId;}
}

