package com.example.echo.echoprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class GameplayActivity extends AppCompatActivity {

    LevelManager levelManager;
    Player player;
    float initialInputCoordinate_X, initialInputCoordinate_Y, finalInputCoordinate_X, finalInputCoordinate_Y;
    Intent navigateToInGameMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        levelManager = LevelManager.get(this);
        player = new Player(this);
        navigateToInGameMenu = new Intent(this, InGameMenu.class);

        if (savedInstanceState != null)
        {
            //player.setPosition();
            //player.setOrientation();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        // WE SHOULD HAVE SOME TEXT TO SPEECH NARRATION THAT INFORMS THE PLAYER THE APP IS PAUSING

    }
    @Override
    protected void onResume() {
        super.onResume();

        // WE SHOULD HAVE SOME TEXT TO SPEECH NARRATION THAT INFORMS THE PLAYER THE APP IS RESUMING
        // AND ALSO REMIND THE PLAYER WHERE IN THE APPLICATION THEY CURRENTLY ARE
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
                initialInputCoordinate_X = event.getX();
                initialInputCoordinate_Y = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                finalInputCoordinate_X = event.getX();
                finalInputCoordinate_Y = event.getY();
                /**
                 * The echolocate capability as explained in the SRS documentation
                 * NOTE: Not fully implemented, this is only the prototype v1.0.1
                 */

                /**
                 * You move forward by swiping up, it plays your "footsteps", and when you
                 * reach the end. It also plays a good song created by Nick.
                 */
                if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                    attemptMoveForward();
                } else if ((initialInputCoordinate_X > finalInputCoordinate_X) && (Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) > 400)) {
                    turnLeft();
                } else if ((finalInputCoordinate_X > initialInputCoordinate_X) && (Math.abs(finalInputCoordinate_X - initialInputCoordinate_X) > 400)) {
                    turnRight();
                } else if ((finalInputCoordinate_Y > initialInputCoordinate_Y) && (Math.abs(finalInputCoordinate_Y - initialInputCoordinate_Y) > 400)) {
                    startActivityForResult(navigateToInGameMenu, 1234);
                } else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                    echolocate();
                }
        }
        return false;
    }


    public void turnLeft()
    {
        // logic from player.turnLeft() should be transferred to here
        //TODO: Tim put your code here
    }

    public void turnRight()
    {
        // logic from player.turnRight() should be transferred to here
        //TODO: Tim put your code here
    }

    public void attemptMoveForward()
    {
        // logic from player.attemptMoveForward() should be transferred to here
        //TODO: Tim put your code here
    }

    public void echolocate()
    {
        // logic from player.echolocate() should be transferred to here
        //TODO: Tim put your code here
    }

    public void initializeCurrentLevel()
    {
        // generate the current level and its items from the corresponding text config file
        ///generateLevelFromConfigFile();

        // start playing the ambient sfx for the current level
        // TODO logic here



    }

    public void completedCurrentLevel()
    {
        // play goal narration


    }

    public void generateLevelFromConfigFile()
    {
        // logic from level.loadLevel() should be transferred here.
        // must be modified to include item generation for the level
        //TODO: Charles put your code here

    }




}
