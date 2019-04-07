package com.example.eduardbolanos642.echoprototype;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;


public class TestGameplay extends AppCompatActivity {

    private Context context = TestGameplay.this;
    float x1, x2, y1, y2;
    Level level;
    Player player = new Player();
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
                    player.echolocate(context, level);
                }


                /**
                 * You move forward by swiping up, it plays your "footsteps", and when you
                 * reach the end. It also plays a good song created by Nick.
                 */
                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    player.attemptMoveForward(context, level);
                    }
                    else if((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                        player.turnLeft(context);
                }
                else if ((x2 > x1) && (Math.abs(x2 - x1) > 400)){
                    player.turnRight(context);
                }
                else if ((y2 > y1) && (Math.abs(y2 - y1) > 400)) {
                    //enter in gameMenu;
                }
        {

        }
        return false;
    }
}

