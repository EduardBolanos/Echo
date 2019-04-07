package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class Level
{
    Tile layout[5][5];


    boolean isLegal(int position[2])
    {
        char location = layout[position[0]][position[1]];
        switch (location) {
            case 'w':
                return false;
            case 'f':
                return true;
            case 'e':
                //exit level progress to next
                return true;
        }
        return false;
    }




}