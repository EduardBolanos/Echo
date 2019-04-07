package com.example.eduardbolanos642.echoprototype;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;


public class TestGameplay extends AppCompatActivity {

    float x1, x2, y1, y2;
    char map[][];
    int countStep =0;
    //current map
    //new player
    //


    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gameplay);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                /**
                 * The echolocate capability as explained in the SRS documentation
                 * NOTE: Not fully implemented, this is only the prototype v1.0.0
                 */
            }
                if (x1 == x2) {
                    //Player.echolocate(getContext());
                }


                /**
                 * You move forward by swiping up, it plays your "footsteps", and when you
                 * reach the end. It also plays a good song created by Nick.
                 */
                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    //Player.attemptMoveForward(getContext());
                    }
                    else if((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                        //Player.turnLeft(getContext)
                }
                else if ((x2 > x1) && (Math.abs(x2 - x1) > 400)){
                    //Player.turnRight(getContext);
                }
                else if ((y2 > y1) && (Math.abs(y2 - y1) > 400)) {
                    //enter in gameMenu;
                }
        {

        }
        return false;
    }
}

