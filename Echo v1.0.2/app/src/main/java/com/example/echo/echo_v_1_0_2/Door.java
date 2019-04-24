package com.example.echo.echo_v_1_0_2;

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
