package com.example.echo.echo_v_1_0_2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class GameplayActivity extends AppCompatActivity {

   private LevelManager levelManager;
   private SoundSettings volumeControl;
   private MediaPlayer turn;
   private MediaPlayer moveForward;
   private MediaPlayer ending;
   private MediaPlayer hitWall;
   private MediaPlayer levelChange;
   private MediaPlayer leftSideWallTap;
   private MediaPlayer rightSideWallTap;
   private MediaPlayer[] echo;
   private MediaPlayer emptySpaceLeft;
   private MediaPlayer emptySpaceRight;
   private MediaPlayer emptySpaceBack;
   private MediaPlayer passing;
   private MediaPlayer narrator;
   private MediaPlayer doorUnlocking;
   private MediaPlayer doorLocked;
   private MediaPlayer slavicDoorHit;
   private MediaPlayer keyJingle;
   private MediaPlayer pickup;
   private MediaPlayer echoDoor;

   private int clearInventory;
   private int flags[] = {0,0,0,0,0,0,0,0,0,0,0};
    String primer = ("android.resource://" + "com.example.echo.echo_v_1_0_2" + "/raw/");
    Uri hammer;
   private Player player;
    float initialInputCoordinate_X, initialInputCoordinate_Y, finalInputCoordinate_X, finalInputCoordinate_Y;
    Intent navigateToInGameMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        levelManager = LevelManager.get(this);
        volumeControl = new SoundSettings();
        clearInventory = 0;
        FileInputStream settings;
        try {
            settings = openFileInput("settings");
            volumeControl.loadSettings(settings);
            settings.close();
        } catch (Exception e) {
            volumeControl.soundFX = 0.6f;
            volumeControl.voiceFX = 1.0f;
            volumeControl.ambianceFX = 0.3f;
            volumeControl.vibrationIntensity = 0.6f;
        }
        narrator = MediaPlayer.create(GameplayActivity.this, R.raw.none);
        turn = MediaPlayer.create(GameplayActivity.this, R.raw.swooshleft);

        moveForward = MediaPlayer.create(GameplayActivity.this, R.raw.genericfootsteps);
        moveForward.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
        hitWall.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        passing = MediaPlayer.create(GameplayActivity.this, R.raw.goalresponse);
        passing.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        doorUnlocking = MediaPlayer.create(GameplayActivity.this, R.raw.unlockingdoor);
        doorUnlocking.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        doorLocked = MediaPlayer.create(GameplayActivity.this, R.raw.doorlocked);
        doorLocked.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        slavicDoorHit = MediaPlayer.create(GameplayActivity.this, R.raw.slav01);
        slavicDoorHit.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        pickup = MediaPlayer.create(GameplayActivity.this, R.raw.okay);
        pickup.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        //TODO add the door noise correctly
        echoDoor = MediaPlayer.create(GameplayActivity.this, R.raw.yeah);
        echoDoor.setVolume(volumeControl.soundFX,volumeControl.soundFX);


        echo = new MediaPlayer[3]; // forward echo
        echo[0] = new MediaPlayer();
        echo[1] = new MediaPlayer();
        echo[2] = new MediaPlayer();
        echo[0] = MediaPlayer.create(GameplayActivity.this, R.raw.echolocate);
        echo[1] = MediaPlayer.create(GameplayActivity.this, R.raw.echolocate);
        echo[2] = MediaPlayer.create(GameplayActivity.this, R.raw.echolocate);
        echo[0].setVolume(volumeControl.soundFX, volumeControl.soundFX);
        echo[1].setVolume(volumeControl.soundFX, volumeControl.soundFX);
        echo[2].setVolume(volumeControl.soundFX, volumeControl.soundFX);

        leftSideWallTap = MediaPlayer.create(GameplayActivity.this, R.raw.wall03_tin_ting);
        leftSideWallTap.setVolume((volumeControl.soundFX*100)/400, 0);

        rightSideWallTap = MediaPlayer.create(GameplayActivity.this, R.raw.wall05_steel_bonk);
        rightSideWallTap.setVolume(0,(volumeControl.soundFX*100)/400);

        emptySpaceLeft = MediaPlayer.create(GameplayActivity.this, R.raw.empty_space_left);;
        emptySpaceRight = MediaPlayer.create(GameplayActivity.this, R.raw.empty_space_right);
        emptySpaceBack = MediaPlayer.create(GameplayActivity.this, R.raw.empty_space);
        emptySpaceLeft.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        emptySpaceRight.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        emptySpaceBack.setVolume((volumeControl.soundFX * 100) / 200, (volumeControl.soundFX * 100) / 0200);

        levelChange = MediaPlayer.create(this, R.raw.nextlevel);
        levelChange.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        ending = MediaPlayer.create(GameplayActivity.this, R.raw.beatingitup2);
        ending.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);

        player = new Player(this);
        String newGameState = getIntent().getExtras().getString("gameState");
        if(newGameState.equals("yes")){
        generateLevelFromConfigFile( levelManager.getLevel(0), false);
        }
        else if(newGameState.equals("no")){
            generateLevelFromConfigFile("saveGame", true);
        }
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
        navigateToInGameMenu = new Intent(this, InGameMenu.class);
        Runtime r = Runtime.getRuntime();
        r.gc();
        if (savedInstanceState != null)
        {
            //player.setPosition();
            //player.setOrientation();
        }
    }

    @Override
    protected void onPause(){
        this.saveGame();
        this.saveItems();
        narrator.release();
        clearInventory = 0;
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
            FileInputStream settings;
            try {
                settings = openFileInput("settings");
                volumeControl.loadSettings(settings);
                settings.close();
            } catch (Exception e) {
                volumeControl.soundFX = 0.6f;
                volumeControl.voiceFX = 1.0f;
                volumeControl.ambianceFX = 0.3f;
                volumeControl.vibrationIntensity = 0.6f;
            }
            this.generateLevelFromConfigFile("saveGame",true);
            narrator = MediaPlayer.create(this, R.raw.none);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            boolean test = data.getBooleanExtra("resetLevel", false);
            if(test){
                this.resetLevel();
            }
            test = data.getBooleanExtra("ShutOffState", false);
            if(test){
                finish();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        Runtime r = Runtime.getRuntime();
        r.gc();
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
                 */
                // TODO LOGIC for TUTORIAL
                if(levelManager.getCurrentLevel() == 1){
                    if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                        attemptMoveForward();
                    } else if ((finalInputCoordinate_Y > initialInputCoordinate_Y) && (Math.abs(finalInputCoordinate_Y - initialInputCoordinate_Y) > 400)) {
                    startActivityForResult(navigateToInGameMenu, 1234);
                }

                }else if(levelManager.getCurrentLevel() == 2){
                    if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                        if (flags[0] == 1) {
                            attemptMoveForward();
                        }
                    }
                    else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                        echolocate();
                        if(flags[0] == 0){
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.twovoice);
                            r = Runtime.getRuntime();
                            r.gc();
                            narrator.start();
                            flags[0] = 1;
                            // HERE
                        } else if ((finalInputCoordinate_Y > initialInputCoordinate_Y) && (Math.abs(finalInputCoordinate_Y - initialInputCoordinate_Y) > 400)) {
                            startActivityForResult(navigateToInGameMenu, 1234);
                        }
                    }
                }
                /**
                 * You move forward by swiping up, it plays your "footsteps", and when you
                 * reach the end. It also plays a good song created by Nick.
                 */
                else {
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
        }
        return false;
    }



    public void turnLeft()
    { // player turns left
        Runtime r = Runtime.getRuntime();
        r.gc();
        if(player.getOrientation() == 0)
        {
            player.setOrientation(3);
        }
        else
        {
            player.setOrientation((player.getOrientation())-1);
        }
        //play turn
        turn = MediaPlayer.create(this, R.raw.swooshleft);
        turn.setVolume(volumeControl.soundFX, 0);
        turn.start();
    }

    public void turnRight()
    {
        // logic from player.turnRight() should be transferred to here
        Runtime r = Runtime.getRuntime();
        r.gc();
        if(player.getOrientation() == 3)
        {
            player.setOrientation(0);
        }
        else
        {
            player.setOrientation((player.getOrientation())+1);
        }
        //play turn
        turn = MediaPlayer.create(this, R.raw.swooshright);
        turn.setVolume(0, volumeControl.soundFX);
        turn.start();
    }

    public void attemptMoveForward()
    {
        // logic from player.attemptMoveForward() should be transferred to here
        Runtime r = Runtime.getRuntime();
        r.gc();
        int[] newPosition;
        int[] position = player.getPosition();
        newPosition = moveFromPosition(player.getOrientation(),position);
        if(levelManager.isLegal(newPosition))
        {
            //play footstep
            moveForward.start();
            player.setPosition(newPosition);
            if(levelManager.getTileAtCoord(newPosition).getType() == 'e')
            {
               ending.start();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                ending.stop();
                ending.reset();
                // LOGIC FOR TUTORIAL
                switch (levelManager.getCurrentLevel()){
                    case 1:
                        narrator.release();
                        narrator = MediaPlayer.create(this, R.raw.tutonevoice);
                        r = Runtime.getRuntime();
                        r.gc();
                        narrator.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                        narrator.start();
                        try {
                            Thread.sleep(12000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        narrator.stop();
                        break;
                    case 3:
                        narrator.release();
                    narrator = MediaPlayer.create(this, R.raw.tutthreevoice);
                        r = Runtime.getRuntime();
                        r.gc();
                    narrator.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                    narrator.start();
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    narrator.stop();
                    break;
                }
                levelChange.start();
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                levelChange.stop();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                clearInventory = 1;
                levelChange = MediaPlayer.create(this, R.raw.levelchange);
                levelChange.setVolume(volumeControl.soundFX, volumeControl.soundFX);

                ending = MediaPlayer.create(GameplayActivity.this, R.raw.beatingitup);
                ending.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);
                r = Runtime.getRuntime();
                r.gc();
                this.nextLevel();
            }
            //if tile is item get item, if tile is end play end
        }
        else {
            //play wall hit
            if(levelManager.getTileAtCoord(newPosition).getType() == 'w')
            {
                hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
                hitWall.setVolume(volumeControl.soundFX,volumeControl.soundFX);
                hitWall.start();
            }
            else if(levelManager.getTileAtCoord(newPosition).getType() == 'd')
            {
                if(levelManager.openDoor(newPosition)){

                    doorUnlocking.start();
                    player.setPosition(newPosition);
                    moveForward.start();
                }
                else {
                    slavicDoorHit.setVolume(volumeControl.soundFX,volumeControl.soundFX);
                    slavicDoorHit.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    doorLocked.setVolume(volumeControl.soundFX,volumeControl.soundFX);
                    doorLocked.start();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    hammer = Uri.parse(primer + "keys0" + levelManager.getDoorAtPosition(newPosition).getPasscode());
                    keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);

                }
            }
            //if(Map.isLegal(newPosition)) == false  dont move forward play tileSound
        }
        if(levelManager.hasKey(newPosition)){
            if(levelManager.pickUpKey(newPosition)) {
                // TODO Maybe pickup noise?, temp pickup noise btw
                pickup.setVolume(volumeControl.soundFX,volumeControl.soundFX);
                pickup.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(newPosition).getPassCode());
                keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                keyJingle.start();
            }
            //other then that, you have already picked it up

        }
    }

    public void echolocate() { // Reason why this is so big is because sounds need to be differentated, AKA
        // places adj to player location sounds are different from echolocate pings far in the distance.
        // logic from player.echolocate() should be transferred to here
        Runtime r = Runtime.getRuntime();
        r.gc();
        char leftTile;
        char rightTile;
        int echoTurnState = 0;
        int[] newPosition;
        int[] leftPosition;
        int[] rightPosition;
        int[] backPosition;
        int[] position = player.getPosition();
        int orientation = player.getOrientation();
        newPosition = moveFromPosition(orientation, position);
        int leftOrientation;
        if (orientation == 0) {
            leftOrientation = 3;
        } else {
            leftOrientation = orientation - 1;
        }
        leftPosition = moveFromPosition(leftOrientation, position);
        int rightOrientation;
        if (orientation == 3) {
            rightOrientation = 0;
        } else {
            rightOrientation = orientation + 1;
        }
        rightPosition = moveFromPosition(rightOrientation, position);
        int backOrientation;
        if (rightOrientation == 3) {
            backOrientation = 0;
        } else {
            backOrientation = orientation + 1;
        }
        backPosition = moveFromPosition(backOrientation, position);
        leftTile = levelManager.getTileAtCoord(leftPosition).getType(); // What is near player's left side?
        if (leftTile == 'e') {
            passing.setVolume(volumeControl.soundFX, 0);
            passing.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (leftTile == 'w') {
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume(volumeControl.soundFX, 0);
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (leftTile == 'f') {
            emptySpaceLeft.start();
            //stop playing leftSound
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (leftTile == 'd') {
            echoDoor.setVolume(volumeControl.soundFX,0);
            echoDoor.start();
            //stop playing leftSound
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        if(levelManager.hasKey(leftPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(leftPosition).getPassCode());
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        rightTile = levelManager.getTileAtCoord(rightPosition).getType(); // What is near the players' right side?
        if (rightTile == 'e') {
            passing.setVolume(0, volumeControl.soundFX);
            passing.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (rightTile == 'w') {
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume(0, volumeControl.soundFX);
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (rightTile == 'f') {
            emptySpaceLeft.setVolume(0, volumeControl.soundFX);
            emptySpaceLeft.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            //stop playing rightSound
        } else if (rightTile == 'd') {
            echoDoor.setVolume(0,volumeControl.soundFX);
            echoDoor.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        if(levelManager.hasKey(rightPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(rightPosition).getPassCode());
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        char backTile = levelManager.getTileAtCoord(backPosition).getType(); // what is near the player's back?
        if (backTile == 'e') {
            passing.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);
            passing.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (backTile == 'w') {
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (backTile == 'f') {
            emptySpaceBack.setVolume(volumeControl.soundFX, volumeControl.soundFX);
            emptySpaceBack.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            //stop playing rightSound
        } else if (backTile == 'd') {
            echoDoor.setVolume((volumeControl.soundFX*100)/200,(volumeControl.soundFX*100)/200);
            echoDoor.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        if(levelManager.hasKey(backPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(backPosition).getPassCode());
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        int playOnce = 0;
        int volumePower = 100;
        echo[0].setVolume(volumeControl.soundFX, volumeControl.soundFX); // volume defaults
        echo[1].setVolume(volumeControl.soundFX, volumeControl.soundFX);
        echo[2].setVolume(volumeControl.soundFX, volumeControl.soundFX);
        while (levelManager.isLegal(newPosition)) //is legal?
        {
            if (playOnce == 1) { // prevents sound from player left and right
                leftPosition = moveFromPosition(leftOrientation, newPosition);
                leftTile = levelManager.getTileAtCoord(leftPosition).getType(); // checks left area
                if (leftTile == 'e') {
                    passing.setVolume((volumeControl.soundFX*100)/200, 0);
                    passing.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'w') {
                    leftSideWallTap.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'f') {
                    emptySpaceLeft.start();
                    //stop playing leftSound
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'd') {
                    echoDoor.setVolume((volumeControl.soundFX*100)/200,0);
                    echoDoor.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                if(levelManager.hasKey(leftPosition)){
                    hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(leftPosition).getPassCode());
                    keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                    keyJingle.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                rightPosition = moveFromPosition(rightOrientation, newPosition);
                rightTile = levelManager.getTileAtCoord(rightPosition).getType(); // checks right area
                if (rightTile == 'e') {
                    passing.setVolume(0, (volumeControl.soundFX*200)/100);
                    passing.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'w') {
                    rightSideWallTap.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'f') {
                    emptySpaceRight.start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    //stop playing rightSound
                } else if (rightTile == 'd') {
                    echoDoor.setVolume(0,(volumeControl.soundFX*100)/200);
                    echoDoor.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                if(levelManager.hasKey(rightPosition)){
                    hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(rightPosition).getPassCode());
                    keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                    keyJingle.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            } else {
                playOnce = 1; // now starts checking left and right
            }
            //play distance ping(tile sound)
//            echo.stop();
            if (echoTurnState == 3) { // cycles ping sound
                echoTurnState = 0;
            }
            echo[echoTurnState].start();
            if (volumePower > 25) { //reduce volume
                volumePower = volumePower - 5;
                echo[0].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
                echo[1].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
                echo[2].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
            }
            echoTurnState++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (levelManager.getTileAtCoord(newPosition).getType() == 'e') {
//                echo.stop();
                passing.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                passing.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            if(levelManager.hasKey(newPosition)){
                hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(newPosition).getPassCode());
                keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                keyJingle.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            newPosition = moveFromPosition(orientation, newPosition);
        }
        if (echoTurnState == 3) {
            echoTurnState = 0;
        }
        echo[echoTurnState].start();
        if (volumePower > 25) {
            volumePower = volumePower - 5;
            echo[0].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
            echo[1].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
            echo[2].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
        }
        echoTurnState++;
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        backTile = levelManager.getTileAtCoord(newPosition).getType(); // using backtile as forwardtile,
         if (backTile == 'w') {
             //play wall
             hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
             hitWall.setVolume((volumeControl.soundFX * 100) / 200, (volumeControl.soundFX * 100) / 200);
             // 1/2 volume for forward wall
             hitWall.start();
             try {
                 Thread.sleep(100);
             } catch (InterruptedException ex) {
                 Thread.currentThread().interrupt();
             }
         } else if (backTile == 'd') {
            echoDoor.setVolume((volumeControl.soundFX * 100) / 200, (volumeControl.soundFX * 100) / 200);
            echoDoor.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
            if(playOnce == 1) {
                //go back to legal space
                newPosition = moveFromPosition(backOrientation, newPosition);
                //play sounds to identify left
                leftPosition = moveFromPosition(leftOrientation, newPosition);
                if(leftPosition[0] == -1){ // some glitch and bug
                    leftPosition[0] = 0;
                }
                if(leftPosition[1] == -1){
                    leftPosition[1] = 0;
                }
                leftTile = levelManager.getTileAtCoord(leftPosition).getType();
                if (leftTile == 'e') {
                    passing.setVolume((volumeControl.soundFX*100)/200, 0);
                    passing.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'w') {
                    hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
                    hitWall.setVolume(volumeControl.soundFX, 0);
                    hitWall.start();
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'f') {
                    emptySpaceLeft.setVolume(volumeControl.soundFX, 0);
                    emptySpaceLeft.start();
                    //stop playing leftSound
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'd') {
                    echoDoor.setVolume((volumeControl.soundFX*100)/400,0);
                    echoDoor.start();
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    if(levelManager.hasKey(leftPosition)){
                        hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(leftPosition).getPassCode());
                        keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                        keyJingle.setVolume((volumeControl.soundFX*volumePower)/200,0);
                        keyJingle.start();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                //play sounds to identify right
                rightPosition = moveFromPosition(rightOrientation, newPosition);
                rightTile = levelManager.getTileAtCoord(rightPosition).getType();
                if (rightTile == 'e') {
                    passing.setVolume(0, (volumeControl.soundFX*100)/200);
                    passing.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'w') {
                    hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
                    hitWall.setVolume(0, volumeControl.soundFX);
                    hitWall.start();
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'f') {
                    emptySpaceRight.setVolume(0, volumeControl.soundFX);
                    emptySpaceRight.start();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    //stop playing rightSound
                } else if (rightTile == 'd') {
                    echoDoor.setVolume(0,(volumeControl.soundFX*100)/400);
                    echoDoor.start();
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                if(levelManager.hasKey(rightPosition)){
                    hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(rightPosition).getPassCode());
                    keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                    keyJingle.setVolume(0,(volumeControl.soundFX*volumePower)/200);
                    keyJingle.start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            r = Runtime.getRuntime();
            r.gc();
        }

    public int[] moveFromPosition(int orientation, int[] position){
        int[] newPosition = new int[2];
        switch (orientation) {
            case 0:
                newPosition[0] = position[0];
                newPosition[1] = position[1] + 1;
                break;
            case 1:
                newPosition[0] = position[0] + 1;
                newPosition[1] = position[1];
                break;
            case 2:
                newPosition[0] = position[0];
                newPosition[1] = position[1] - 1;
                break;
            case 3:
                newPosition[0] = position[0] - 1;
                newPosition[1] = position[1];
                break;
        }
        return newPosition;
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

    public void generateLevelFromConfigFile(String levelName, boolean saveGameStatus) {
        // logic from level.loadLevel() should be transferred here.
        // save determines if in asset or file
        int data = 0;
        String concatinator = "";
        int[] playerPos = new int[2];
        int state = 0;
        int locX = 0;
        int locY = 0;
        int bypass = 0;
        ArrayList<Item> refer = new ArrayList<Item>();
        int keyLoc;
        Tile map[][] = null;
        InputStream asset = null;
        FileInputStream assetTwo = null;
        ArrayList<Integer> passCodes = new ArrayList<Integer>();
        levelManager.resetDoors();
        if(!saveGameStatus) {
            try {
                asset = this.getAssets().open(levelName);
            } catch (java.io.IOException e) {
            }
        }else{
            try {
                assetTwo = openFileInput(levelName);
            } catch (java.io.IOException e) {
            }
        }

        if (asset != null || assetTwo != null) {
            while (data != -1) {
                if(!saveGameStatus) {
                    try {
                        data = asset.read();
                    } catch (java.io.IOException e) {
                    }
                }else{
                    try {
                        data = assetTwo.read();
                    } catch (java.io.IOException e) {
                    }
                }
                if (((char) data != '\n' && ((char) data > 31))) {
                    switch (state) {
                        case 0:
                            if (data != 35) {
                                concatinator = concatinator + ((char) data);
                            } else {
                                levelManager.setCurrentLevel(Integer.parseInt(concatinator));
                                concatinator = "";
                                state = 1;
                            }
                            break;
                        case 1:
                            if (data != 35) {
                                if (bypass == 1) {
                                    concatinator = concatinator + ((char) data);
                                } else {
                                    if (data != 32 && bypass == 0) {
                                        concatinator = concatinator + ((char) data);
                                    } else {
                                        levelManager.sizeX = Integer.parseInt(concatinator);
                                        concatinator = "";
                                        bypass = 1;
                                    }
                                }
                            } else {
                                levelManager.sizeY = Integer.parseInt(concatinator);
                                map = new Tile[levelManager.sizeX][levelManager.sizeY];
                                locY = levelManager.sizeY - 1;
                                concatinator = "";
                                state = 2;
                            }
                            break;
                        case 2:
                            if (data != 35) {
                                switch (data) {
                                    case 'f': // a walkable floor
                                        map[locX][locY] = levelManager.floor;
                                        break;
                                    case 'e': // the goal
                                        map[locX][locY] = levelManager.end;
                                        int[] ep = {locX, locY};
                                        levelManager.setPlayerSpawnPosition(ep);
                                        break;
                                    case 'w': // adds a wall
                                        map[locX][locY] = levelManager.wall;
                                        break;
                                    case 'P': // initiates player spawn
                                        map[locX][locY] = levelManager.floor;
                                        playerPos[0] = locX; playerPos[1] = locY;
                                        levelManager.setPlayerSpawnPosition(playerPos);
                                        break;
                                    case 'd': // adds a door
                                        map[locX][locY] = levelManager.door;
                                        while (data != ')') {
                                            if (!saveGameStatus) {
                                                try {
                                                    data = asset.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            } else {
                                                try {
                                                    data = assetTwo.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            }
                                            if (data != '(' && data != ')') {
                                                concatinator = concatinator + ((char) data);
                                            } else if (data != '(') {
                                                int[] doorPos = {locX, locY};
                                                StringBuilder doorCode = new StringBuilder();
                                                doorCode.append(concatinator);
                                                Door aDoor = new Door(doorPos, doorCode.toString());
                                                levelManager.addDoor(aDoor);
                                                concatinator = "";
                                            }
                                        }
                                        break;
                                    case 'k':
                                        map[locX][locY] = levelManager.floor;
                                        while (data != ')') {
                                            if (!saveGameStatus) {
                                                try {
                                                    data = asset.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            } else {
                                                try {
                                                    data = assetTwo.read();
                                                } catch (java.io.IOException e) {
                                                }
                                            }
                                            if (data != '(' && data != ')') {
                                                concatinator = concatinator + ((char) data);
                                            } else if (data != '(') {
                                                if(data == 'P'){
                                                    map[locX][locY] = levelManager.floor;
                                                    playerPos[0] = locX; playerPos[1] = locY;
                                                    levelManager.setPlayerSpawnPosition(playerPos);
                                                }
                                                int[] keyPos = {locX, locY};
                                                StringBuilder keyCode = new StringBuilder();
                                                keyCode.append(concatinator);
                                                Item aKey = new Item(keyCode.toString(), keyPos, 0); // 0 is key
                                                refer.add(aKey);
                                                concatinator = "";
                                            }
                                        }
                                        break;

                                }
                                locX++;
                            } else {
                                locY--;
                                locX = 0;
                            }
                            if (locY < 0) {
                                levelManager.setMap(map);
                                state = 3;
                            }
                            break;
                        case 3:
                            concatinator = "";
                            concatinator = concatinator + ((char) data);
                            levelManager.setPlayerSpawnOrientation(Integer.parseInt(concatinator));
                            concatinator = "";
                            if (!saveGameStatus) {// plays a new level intro
                                try {
                                    data = asset.read();
                                } catch (java.io.IOException e) {
                                }
                                while (data != -1) {
                                    try {
                                        data = asset.read();
                                    } catch (java.io.IOException e) {
                                    }
                                    if (((char) data != '\n' && ((char) data > 31))) {
                                        if (data != 35) {
                                            concatinator = concatinator + ((char) data);
                                        } else {
                                            hammer = Uri.parse(primer + concatinator); // intro lines
                                            narrator.release();
                                            narrator = MediaPlayer.create(this, hammer);
                                            narrator.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                                            narrator.start();
                                            concatinator = "";
                                            data = -1;
                                        }
                                    }
                                }
                            }
                            if (refer.size() > 0) {
                                if (saveGameStatus) {
                                    try {
                                        data = assetTwo.read();
                                    } catch (java.io.IOException e) {
                                    }
                                }
                            }
                            concatinator = "";
                            state = 0;
                            keyLoc = 0;
                            int end = 1;
                            for (int x = 0; x < refer.size(); x++) { // adding items
                                end = 1;
                                while (end == 1) {
                                    if (!saveGameStatus) {
                                        try {
                                            data = asset.read();
                                        } catch (java.io.IOException e) {
                                        }
                                    } else {
                                        try {
                                            data = assetTwo.read();
                                        } catch (java.io.IOException e) {
                                        }
                                    }
                                    if (((char) data != '\n' && ((char) data > 31))) {
                                        if (data != 35) {
                                            concatinator = concatinator + ((char) data);
                                        } else {
                                            switch (state) {
                                                case 0:
                                                    for (int y = 0; y < refer.size(); y++) {
                                                        keyLoc = y;
                                                        if(refer.get(y).getPassCode().equals(concatinator)){
                                                            break;
                                                        }
                                                    }
                                                    concatinator = "";
                                                    state = 1;
                                                    break;
                                                case 1:
                                                    refer.get(keyLoc).setName(concatinator);
                                                    concatinator = "";
                                                    state = 2;
                                                    break;
                                                case 2:
                                                    refer.get(keyLoc).setAuditoryId(concatinator);
                                                    concatinator = "";
                                                    state = 3;
                                                    break;
                                                case 3:
                                                    concatinator = "";
                                                    state = 4;
                                                    break;
                                                case 4:
                                                    refer.get(keyLoc).setStatus(Integer.parseInt(concatinator));
                                                    concatinator = "";
                                                    state = 0;
                                                    keyLoc = 0;
                                                    end = 0;
                                            }
                                        }
                                    }
                                }
                            }
                            levelManager.setItemsToSpawn(refer);
                            data = -1;
                    }
                    }
                }
            }

        }

 public void saveGame(){
         FileOutputStream saveGame;
         String parser;
         int stringSizeTracker = 0;
         try {
             saveGame = openFileOutput("saveGame", this.MODE_PRIVATE);
             parser = Integer.toString(levelManager.getCurrentLevel());
             while (stringSizeTracker < parser.length()) {
                 try {
                     saveGame.write(parser.charAt(stringSizeTracker));
                     stringSizeTracker++;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             stringSizeTracker = 0;
             saveGame.write('#');
             parser = Integer.toString(levelManager.sizeX);
             while (stringSizeTracker < parser.length()) {
                 try {
                     saveGame.write(parser.charAt(stringSizeTracker));
                     stringSizeTracker++;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             stringSizeTracker = 0;
             saveGame.write(' ');
             parser = Integer.toString(levelManager.sizeY);
             while (stringSizeTracker < parser.length()) {
                 try {
                     saveGame.write(parser.charAt(stringSizeTracker));
                     stringSizeTracker++;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             saveGame.write('#');
             int mPlayerSpawnPoint[] = player.getPosition();
             Tile mMap[][] = levelManager.getMap();
             int checker[]= new int[2];
             for(int y = levelManager.sizeY - 1; y >= 0 ; y--){
                for(int x = 0; x < levelManager.sizeX; x++){
                    // if(mEndPoint[0] == x && mEndPoint[1] == y){
                    //     saveGame.write(levelManager.end.getType());
                   //  }
                     //else
                    checker[0] = x; checker[1] = y;

                     if(levelManager.hasKey(checker)){
                         saveGame.write('k');
                        saveGame.write('(');
                        for(int i = 0; i < levelManager.getItemArraySize(); i++){
                            if(levelManager.getItem(i).getLocation()[0] == x && levelManager.getItem(i).getLocation()[1] == y){
                                parser = levelManager.getItem(i).getPassCode();
                                stringSizeTracker = 0;
                                while (stringSizeTracker < parser.length()) {
                                    try {
                                        saveGame.write(parser.charAt(stringSizeTracker));
                                        stringSizeTracker++;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                         if(mPlayerSpawnPoint[0] == x && mPlayerSpawnPoint[1] == y){
                             saveGame.write('P');}
                        saveGame.write(')');
                    } else if(mPlayerSpawnPoint[0] == x && mPlayerSpawnPoint[1] == y){
                        saveGame.write('P');
                    }
                    else if(mMap[x][y].getType() == 'd'){
                        saveGame.write('d');
                        saveGame.write('(');
                        for(int i = 0; i < levelManager.getDoorArraySize(); i++){
                            if(levelManager.getDoor(i).mLocation[0] == x && levelManager.getDoor(i).mLocation[1] == y){
                                parser = levelManager.getDoor(i).getPasscode();
                                stringSizeTracker = 0;
                                while (stringSizeTracker < parser.length()) {
                                    try {
                                        saveGame.write(parser.charAt(stringSizeTracker));
                                        stringSizeTracker++;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        saveGame.write(')');
                    }
                     else {
                         saveGame.write(mMap[x][y].getType());
                     }
                 }
                 saveGame.write('#');
             }
             parser = Integer.toString(player.getOrientation());
             saveGame.write(parser.charAt(0));
             saveGame.write('#');
             for(int x = 0; x < levelManager.getItemArraySize(); x++){
                 parser = levelManager.getItem(x).getPassCode();
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveGame.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveGame.write('#');
                 parser = levelManager.getItem(x).getName();
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveGame.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveGame.write('#');
                 parser = levelManager.getItem(x).getAuditoryId();
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveGame.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveGame.write('#');
                 parser = "none"; // STUB TactileID
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveGame.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveGame.write('#');
                 parser = Integer.toString(levelManager.getItem(x).getStatus()); // STUB TactileID
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveGame.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveGame.write('#');
             }
             saveGame.close();
         } catch (Exception e) {
        e.printStackTrace();
        }
     }

     public void saveItems() {
         FileOutputStream saveItems;
         String parser;
         int stringSizeTracker = 0;
         try {
             saveItems = openFileOutput("items", this.MODE_PRIVATE);
             if(clearInventory == 1){
                 saveItems.write('R');
             }
             for(int x = 0; x < levelManager.getItemArraySize(); x++){
                 parser = levelManager.getItem(x).getPassCode();
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveItems.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveItems.write('#');
                 parser = levelManager.getItem(x).getName();
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveItems.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveItems.write('#');
                 parser = levelManager.getItem(x).getAuditoryId();
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveItems.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveItems.write('#');
                 parser = "none"; // STUB TactileID
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveItems.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveItems.write('#');
                 parser = Integer.toString(levelManager.getItem(x).getStatus()); // STUB TactileID
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveItems.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveItems.write('#');
                 parser = Integer.toString(levelManager.getItem(x).gettype()); // STUB TactileID
                 stringSizeTracker = 0;
                 while (stringSizeTracker < parser.length()) {
                     try {
                         saveItems.write(parser.charAt(stringSizeTracker));
                         stringSizeTracker++;
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 saveItems.write('#');
             }
             saveItems.write('@');
             saveItems.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    public void nextLevel(){
        generateLevelFromConfigFile(levelManager.getLevel(levelManager.getCurrentLevel()), false);
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
    }

    public void resetLevel(){
        generateLevelFromConfigFile(levelManager.getLevel(levelManager.getCurrentLevel()-1), false);
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
    }
}
