package com.example.eduardbolanos642.echoprototype;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;


public class TestGameplay extends AppCompatActivity {

    float x1, x2, y1, y2;
    int countStep = 6; //To be increased/decreased later on
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
                if (x1 == x2) {
                    mediaPlayer = MediaPlayer.create(TestGameplay.this, R.raw.echolocate);
                    mediaPlayer.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    mediaPlayer = MediaPlayer.create(TestGameplay.this, R.raw.goalreponse);
                    mediaPlayer.start();

                }/**
                 * You move forward by swiping up, it plays your "footsteps", and when you
                 * reach the end. It also plays a good song created by Nick.
                 */
                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    countStep--;
                    if (countStep >= 0) {
                        mediaPlayer = MediaPlayer.create(TestGameplay.this, R.raw.genericfootsteps); //Pretty good footsteps
                        mediaPlayer.start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (countStep == 0) {
                        mediaPlayer = MediaPlayer.create(TestGameplay.this, R.raw.goalreponse); //Yeah you did it, you still suck at video games
                        mediaPlayer.start();                                                            //RNC FLEEEX
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        mediaPlayer = MediaPlayer.create(TestGameplay.this, R.raw.darknesh); //Cringiest thing of all time
                        mediaPlayer.start();
                        try {
                            Thread.sleep(13000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        mediaPlayer = MediaPlayer.create(TestGameplay.this, R.raw.my_jam);
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true); //looping my_jam, greatest song of March 2019
                    }
                }
        }
        return false;
    }
}

