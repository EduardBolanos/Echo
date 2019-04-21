package com.example.echo.echoprototype;

class Door {
        protected int mLocation[];
        private String mPasscode;
    public Door(int location[], String passcode){
        mLocation = location;
        mPasscode = passcode;
    }

    public String getPasscode() {
        return mPasscode;
    }
}
