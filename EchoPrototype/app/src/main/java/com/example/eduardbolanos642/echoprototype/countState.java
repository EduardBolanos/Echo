package com.example.eduardbolanos642.echoprototype;

public class countState {
    private int state;
    public countState(int start){
        state = start;
    }
    public int getState(){
     return state;
    }
    public void setState(int newState){
        state = newState;
    }
}
