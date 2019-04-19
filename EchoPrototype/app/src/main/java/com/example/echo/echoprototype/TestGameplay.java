package com.example.echo.echoprototype;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


public class TestGameplay extends AppCompatActivity {

    float x1, x2, y1, y2;
    int endPoint[] = {4,7};
    Level level = new Level(this);
    Player player = new Player(TestGameplay.this);
    Intent i;
    //


    MediaPlayer mediaPlayer;

    @Override
    protected void onPause(){
        File file = new File(this.getFilesDir(), "saveGame");
        level.saveGame(file);
        super.onPause();

    }
    @Override
    protected void onResume(){
        boolean gameOpenSaveState = getIntent().getBooleanExtra("gameState", false);
        if(gameOpenSaveState) {
            level.loadLevel("level" + 1 + ".txt", null); // stub
        }
        else{
            InputStream inputStream;
            try {
                inputStream = openFileInput("gameSave");
                level.loadLevel("saveGame", inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();

    }
    @Override
    protected void onDestroy() {
        File file = new File(this.getFilesDir(), "saveGame");
        level.saveGame(file);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gameplay);
        boolean gameOpenSaveState = getIntent().getBooleanExtra("gameState", false);
        if(gameOpenSaveState) {
            level.loadLevel("level" + 1 + ".txt", null); // stub
        }
        else{
            FileInputStream inputStream;
            try {
                inputStream = openFileInput("gameSave");
                level.loadLevel("saveGame", inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        player.setOrientation(level.getStartOrientation());
        player.setPosition(level.getPlayerSpawnPoint());
        i = new Intent(this, InGameMenu.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            boolean test = data.getBooleanExtra("ShutOffState", false);
            if(test){
                finish();
            }
        }
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
                 * NOTE: Not fully implemented, this is only the prototype v1.0.1
                 */


                /**
                 * You move forward by swiping up, it plays your "footsteps", and when you
                 * reach the end. It also plays a good song created by Nick.
                 */
                if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                   player.attemptMoveForward(level);
                } else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)) {
                   player.turnLeft();
                } else if ((x2 > x1) && (Math.abs(x2 - x1) > 400)) {
                   player.turnRight();
                } else if ((y2 > y1) && (Math.abs(y2 - y1) > 400)) {
                    startActivityForResult(i, 1234);
                } else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))) {
                  player.echolocate(level);
                }
        }
        return false;
    }
}

