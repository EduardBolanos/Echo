package com.example.echo.echo_v_1_0_2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class GameplayActivity extends AppCompatActivity {

   private LevelManager levelManager;
   private SoundSettings volumeControl;
   private MediaPlayer turn;
   private MediaPlayer ambiance;
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
   private int deathState;
   public ChopstickMan Nick;
   Toast toast;
   private boolean tutorialLevel;
   private MediaPlayer oof;

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
        levelManager = new LevelManager(this);
        volumeControl = new SoundSettings();
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

        Nick = new ChopstickMan();
        Nick.Stern = true;

        ambiance = MediaPlayer.create(this, R.raw.backgroundcries);
        ambiance.setLooping(true);
        ambiance.setVolume((volumeControl.ambianceFX * 100) / 400, (volumeControl.ambianceFX * 100) / 400);
        ambiance.start();

        narrator = MediaPlayer.create(GameplayActivity.this, R.raw.none);
        turn = MediaPlayer.create(GameplayActivity.this, R.raw.swooshleft);

        moveForward = MediaPlayer.create(GameplayActivity.this, R.raw.genericfootsteps);
        moveForward.setVolume((volumeControl.ambianceFX * 100) / 200, (volumeControl.ambianceFX * 100) / 200);

        hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
        hitWall.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        passing = MediaPlayer.create(GameplayActivity.this, R.raw.goalresponse);
        passing.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        doorUnlocking = MediaPlayer.create(GameplayActivity.this, R.raw.unlockingdoor);
        doorUnlocking.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        doorLocked = MediaPlayer.create(GameplayActivity.this, R.raw.doortry);
        doorLocked.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        slavicDoorHit = MediaPlayer.create(GameplayActivity.this, R.raw.slav01);
        slavicDoorHit.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        pickup = MediaPlayer.create(GameplayActivity.this, R.raw.pickup);
        pickup.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        echoDoor = MediaPlayer.create(GameplayActivity.this, R.raw.neardoor);
        echoDoor.setVolume(volumeControl.soundFX,volumeControl.soundFX);

        oof = MediaPlayer.create(GameplayActivity.this, R.raw.death);
        oof.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        deathState = 3;

        echo = new MediaPlayer[3];
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

        keyJingle = MediaPlayer.create(GameplayActivity.this, R.raw.none);
        player = new Player(this);
        String newGameState = getIntent().getExtras().getString("gameState");
        String tutorial = getIntent().getExtras().getString("tutorialStatus");
        if(tutorial.equals("no")) {
            tutorialLevel = false;
            if (newGameState.equals("yes")) {
                if(getIntent().getExtras().getString("withTutorial").equals("yes")) {
                    generateLevelFromConfigFile(levelManager.getLevel(0), false);
                }
                else{
                    generateLevelFromConfigFile(levelManager.getLevel(8), false);
                }
            } else if (newGameState.equals("no")) {
                generateLevelFromConfigFile("saveGame", true);
            }
        }
        else{
            tutorialLevel = true;
            startTutorialLevel(newGameState);
        }
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
        navigateToInGameMenu = new Intent(this, InGameMenu.class);
    }

    @Override
    protected void onPause(){
        if(!tutorialLevel) {
            this.saveGame();
        }
        this.saveItems();
        ambiance.stop();
        ambiance.release();
        narrator.release();
        super.onPause();

        // TODO WE SHOULD HAVE SOME TEXT TO SPEECH NARRATION THAT INFORMS THE PLAYER THE APP IS PAUSING

    }
    @Override
    protected void onResume() {
        super.onResume();

        // TODO WE SHOULD HAVE SOME TEXT TO SPEECH NARRATION THAT INFORMS THE PLAYER THE APP IS RESUMING
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
            narrator = MediaPlayer.create(this, R.raw.none);
            ambiance = MediaPlayer.create(this, R.raw.backgroundcries);
            ambiance.setLooping(true);
            ambiance.setVolume((volumeControl.ambianceFX * 100) / 400, (volumeControl.ambianceFX * 100) / 400);
            ambiance.start();
            Nick.Stern = true;
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            boolean test = data.getBooleanExtra("resetLevel", false);
            if(test){
                if(tutorialLevel){
                    startTutorialLevel(Integer.toString(levelManager.getCurrentLevel() - 1));
                }
                else {
                    this.resetLevel();
                }
            }
            test = data.getBooleanExtra("ShutOffState", false);
            if(test){
                ambiance.stop();
                ambiance.release();
                narrator.release();
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
                if (Nick.Stern) {
                    if ((finalInputCoordinate_Y > initialInputCoordinate_Y) && (Math.abs(finalInputCoordinate_Y - initialInputCoordinate_Y) > 400)) {
                        startActivityForResult(navigateToInGameMenu, 1234);
                    } else {
                        Nick.Stern = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                /**
                                 * The echolocate capability as explained in the SRS documentation
                                 */
                                switch (levelManager.getCurrentLevel()) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 7:
                                    case 8:
                                        doTutorialCommands();
                                        break;
                                    /**
                                     * You move forward by swiping up, it plays your "footsteps", and when you
                                     * reach the end. Level changes and everything is right in the world.
                                     */
                                    default:
                                        if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                                            attemptMoveForward();
                                        } else if ((initialInputCoordinate_X > finalInputCoordinate_X) && (Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) > 400)) {
                                            turnLeft();
                                        } else if ((finalInputCoordinate_X > initialInputCoordinate_X) && (Math.abs(finalInputCoordinate_X - initialInputCoordinate_X) > 400)) {
                                            turnRight();
                                        } else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                                            echolocate();
                                        }
                                }
                                Nick.Stern = true;
                                Runtime r = Runtime.getRuntime();
                                r.gc();
                            }
                        }).start();
                    }
                }
        }
        super.onTouchEvent(event);
        return false;
    }

    public void doTutorialCommands() {
        switch (levelManager.getCurrentLevel()) {
            case 1:
                if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                    attemptMoveForward();
                    switch (flags[0]) {
                        case 0:
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tutonepartone);
                            narrator.start();
                            flags[0] = 1;
                            break;
                        case 1:
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tutoneparttwo);
                            narrator.start();
                            flags[0] = 2;
                            break;
                        case 2:
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tutonepartthree);
                            narrator.start();
                            flags[0] = 3;
                            break;
                    }
                }
                break;

            case 2:
                if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                    if (flags[1] == 3) {
                        attemptMoveForward();
                    }
                } else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                    echolocate();
                    switch (flags[1]) {
                        case 0:
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tuttwopartone);
                            narrator.start();
                            flags[1] = 1;
                            break;
                        case 1:
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tuttwoparttwo);
                            narrator.start();
                            flags[1] = 2;
                            break;
                        case 2:
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tuttwopartthree);
                            narrator.start();
                            flags[1] = 3;
                            break;
                    }
                }
                break;
            case 3:
                if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                    attemptMoveForward();
                    flags[2]++;
                    if (flags[2] > 3 && flags[3] == 0) {
                        narrator.release();
                        narrator = MediaPlayer.create(GameplayActivity.this, R.raw.swipe);
                        narrator.start();
                        flags[3] = 1;
                    }
                } else if ((finalInputCoordinate_X > initialInputCoordinate_X) && (Math.abs(finalInputCoordinate_X - initialInputCoordinate_X) > 400)) {
                    if (flags[3] == 1) {
                        turnRight();
                    }
                } else if ((initialInputCoordinate_X > finalInputCoordinate_X) && (Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) > 400)) {
                    if (flags[3] == 1) {
                        turnLeft();
                    }
                } else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                    echolocate();
                }
                break;
        case 7:
            if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                if (flags[4] == 1) {
                    attemptMoveForward();
                }
            } else if ((finalInputCoordinate_X > initialInputCoordinate_X) && (Math.abs(finalInputCoordinate_X - initialInputCoordinate_X) > 400)) {
                if (flags[4] == 1) {
                    turnRight();
                }
            } else if ((initialInputCoordinate_X > finalInputCoordinate_X) && (Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) > 400)) {
                if (flags[4] == 1) {
                    turnLeft();
                }
            } else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                echolocate();
                if(flags[4] == 0){
                    narrator.release();
                    narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tutsevenone);
                    narrator.start();
                }
                flags[4] = 1;
            }
        break;
        case 8:
            if ((initialInputCoordinate_Y > finalInputCoordinate_Y) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) > 400)) {
                if (flags[5] == 1) {
                    attemptMoveForward();
                }
            } else if ((finalInputCoordinate_X > initialInputCoordinate_X) && (Math.abs(finalInputCoordinate_X - initialInputCoordinate_X) > 400)) {
                if (flags[5] == 1) {
                    turnRight();
                }
            } else if ((initialInputCoordinate_X > finalInputCoordinate_X) && (Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) > 400)) {
                if (flags[5] == 1) {
                    turnLeft();
                }
            } else if (((Math.abs(initialInputCoordinate_X - finalInputCoordinate_X) < 50) && (Math.abs(initialInputCoordinate_Y - finalInputCoordinate_Y) < 50))) {
                echolocate();
                if(flags[5] == 0){
                    narrator.release();
                    narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tuteightone);
                    narrator.start();
                }
                flags[5] = 1;
            }
        break;
    }
        }


    public void turnLeft()
    {
        if(player.getOrientation() == 0)
        {
            player.setOrientation(3);
        }
        else
        {
            player.setOrientation((player.getOrientation())-1);
        }
        turn.release();
        turn = MediaPlayer.create(this, R.raw.swooshleft);
        turn.setVolume(volumeControl.soundFX, 0);
        turn.start();
    }

    public void turnRight()
    {
        if(player.getOrientation() == 3)
        {
            player.setOrientation(0);
        }
        else
        {
            player.setOrientation((player.getOrientation())+1);
        }
        turn.release();
        turn = MediaPlayer.create(this, R.raw.swooshright);
        turn.setVolume(0, volumeControl.soundFX);
        turn.start();
    }

    public void attemptMoveForward()
    {
        int[] newPosition;
        int[] position = player.getPosition();
        newPosition = player.moveFromPosition(player.getOrientation(),position);
        if(levelManager.isLegal(newPosition))
        {
            moveForward.start();
            player.setPosition(newPosition);
            //TODO TELEPORT newPosition
            int leftOrientation;
            if (player.getOrientation() == 0) {
                leftOrientation = 3;
            } else {
                leftOrientation = player.getOrientation() - 1;
            }
            int[] leftPosition = player.moveFromPosition(leftOrientation, newPosition);
            int rightOrientation;
            if (player.getOrientation() == 3) {
                rightOrientation = 0;
            } else {
                rightOrientation = player.getOrientation() + 1;
            }
            int[] rightPosition = player.moveFromPosition(rightOrientation, newPosition);
            if(levelManager.getTileAtCoord(newPosition).getType() == 'e')
            {
              playEndingLogic();
            }
            else{
                char leftTile = levelManager.getTileAtCoord(leftPosition).getType();
                playWalkingForwardLeftNoise(leftTile, leftPosition);

                char rightTile = levelManager.getTileAtCoord(rightPosition).getType();
                playWalkingForwardRightNoise(rightTile, rightPosition);
            }
        }
        else {
            if(levelManager.getTileAtCoord(newPosition).getType() == 'w')
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.oof).setVisibility(View.VISIBLE);
                    }
                });

                hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
                hitWall.setVolume(volumeControl.soundFX,volumeControl.soundFX);
                hitWall.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hitWallDeathLogic();
                        findViewById(R.id.oof).setVisibility(View.INVISIBLE);
                    }
                });
            }
            else if(levelManager.getTileAtCoord(newPosition).getType() == 'd')
            {
                if(levelManager.openDoor(newPosition)){

                    doorUnlocking.start();
                    player.setPosition(newPosition);
                    moveForward.start();
                    switch (levelManager.getCurrentLevel()) {
                        case 7:
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                            narrator.release();
                            narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tutseventhree);
                            narrator.start();
                            break;
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.oof).setVisibility(View.VISIBLE);
                        }
                    });
                    playDoorHitLogic(newPosition);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.oof).setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }
        //TODO ENEMIES, CHECK HERE
        //TODO ENEMIES, THEN ENEMIES TURN TO MOVE
        if(levelManager.hasKey(newPosition)){
            if(levelManager.pickUpKey(newPosition)) {
                pickup.setVolume(volumeControl.soundFX,volumeControl.soundFX);
                pickup.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(newPosition).getPassCode());
                keyJingle.release();
                keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
                keyJingle.start();
                switch (levelManager.getCurrentLevel()) {
                    case 7:
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        narrator.release();
                        narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tutseventwo);
                        narrator.start();
                        break;
                }
            }
            moveForward.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        }
    }

    private void hitWallDeathLogic() {
        switch (deathState){
            case 3:
                toast = Toast.makeText(this,"You bumped your head. Be more careful.",Toast.LENGTH_SHORT);
                toast.show();
                deathState = 2;
                break;
            case 2:
                toast = Toast.makeText(this,"Your head is hurting bad.",Toast.LENGTH_SHORT);
                toast.show();
                deathState = 1;
                break;
            case 1:
                toast = Toast.makeText(this,"You don't feel like you can take anymore head trauma.",Toast.LENGTH_SHORT);
                toast.show();
                deathState = 0;
                break;
            case 0:
                toast = Toast.makeText(this,"You pass out. Humpty Dumpty.",Toast.LENGTH_SHORT);
                toast.show();
                flags[0] = 0;
                flags[1] = 0;
                flags[2] = 0;
                flags[3] = 0;
                flags[4] = 0;
                flags[5] = 0;
                flags[6] = 0;
                flags[7] = 0;
                oof.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                if(tutorialLevel){
                 startTutorialLevel(Integer.toString(levelManager.getCurrentLevel() - 1));
                }else {
                    resetLevel();
                }
                break;
        }
    }

    private void playDoorHitLogic(int[] newPosition) {
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
        keyJingle.release();
        keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
        keyJingle.start();
    }

    public void nextLevel(){
        deathState = 3;
        generateLevelFromConfigFile(levelManager.getLevel(levelManager.getCurrentLevel()), false);
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
        saveGame();
    }

    public void resetLevel(){
        deathState = 3;
        generateLevelFromConfigFile(levelManager.getLevel(levelManager.getCurrentLevel()-1), false);
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
        saveGame();
    }

    public void startTutorialLevel(String newGameState){
        deathState = 3;
        generateLevelFromConfigFile((newGameState + ".txt"), false);
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
    }

    public void echolocate() { // Reason why this is so big is because sounds need to be differentated, AKA
        // places adj to player location sounds are different from echolocate pings far in the distance.
        char leftTile;
        char rightTile;
        int echoTurnState = 0;
        int[] newPosition;
        int[] leftPosition;
        int[] rightPosition;
        int[] backPosition;
        int[] position = player.getPosition();
        int orientation = player.getOrientation();
        newPosition = player.moveFromPosition(orientation, position);
        //TODO ENEMIES and TELEPORT LOOK FOR play methods and follow their names
        int leftOrientation;

        if (orientation == 0) {
            leftOrientation = 3;
        } else {
            leftOrientation = orientation - 1;
        }
        leftPosition = player.moveFromPosition(leftOrientation, position);
        int rightOrientation;
        if (orientation == 3) {
            rightOrientation = 0;
        } else {
            rightOrientation = orientation + 1;
        }
        rightPosition = player.moveFromPosition(rightOrientation, position);
        int backOrientation;
        if (rightOrientation == 3) {
            backOrientation = 0;
        } else {
            backOrientation = orientation + 1;
        }
        backPosition = player.moveFromPosition(backOrientation, position);
        backPosition = levelManager.checkOutOfBounds(backPosition);

        leftTile = levelManager.getTileAtCoord(leftPosition).getType();
        leftPosition = levelManager.checkOutOfBounds(leftPosition);
        playLeftofPlayer(leftTile, leftPosition);

        rightTile = levelManager.getTileAtCoord(rightPosition).getType();
        rightPosition = levelManager.checkOutOfBounds(rightPosition);
        playRightofPlayer(rightTile, rightPosition);

        char backTile = levelManager.getTileAtCoord(backPosition).getType();
        playBehindPlayer(backTile, backPosition);

        int playOnce = 0;
        int volumePower = 100;

        if(levelManager.isLegal(newPosition)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
                    rippleBackground.startRippleAnimation();
                }
            });
        }

        while (levelManager.isLegal(newPosition))
        {
            if (echoTurnState == 3) {
                echoTurnState = 0;
            }
            if (volumePower > 25 && playOnce == 1) {
                volumePower = volumePower - 15;
            }
            echo[echoTurnState].setVolume((volumeControl.soundFX * volumePower) / 100, (volumeControl.soundFX * volumePower) / 100);
            echo[echoTurnState].start();
            echoTurnState++;

            try {
                Thread.sleep(600);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            playPassingSounds(newPosition);
            newPosition = player.moveFromPosition(orientation, newPosition);
            playOnce = 1;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
                if (rippleBackground.isRippleAnimationRunning()) {
                    rippleBackground.stopRippleAnimation();
                }
            }
        });
        backTile = levelManager.getTileAtCoord(newPosition).getType(); // using backtile as forwardtile,
        playEndForwardTile(backTile);
            if(playOnce == 1) {
                newPosition = player.moveFromPosition(backOrientation, newPosition);
                leftPosition = player.moveFromPosition(leftOrientation, newPosition);
                leftPosition = levelManager.checkOutOfBounds(leftPosition);
                leftTile = levelManager.getTileAtCoord(leftPosition).getType();
                playEndLeftTile(leftTile, leftPosition, volumePower);
                rightPosition = player.moveFromPosition(rightOrientation, newPosition);
                rightPosition = levelManager.checkOutOfBounds(rightPosition);
                rightTile = levelManager.getTileAtCoord(rightPosition).getType();
                playEndRightTile(rightTile, rightPosition, volumePower);
            }
            hitWall.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        }

    /*******************************************************************************************************************************************************
     *Don't look down here, the internals of the code. Clean, but not as clean.
     */

    private void playWalkingForwardRightNoise(char rightTile, int[] rightPosition) {
        switch(rightTile){
            case 'w':
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.rightIndicator).setVisibility(View.VISIBLE);
                    }
                });
                rightSideWallTap.start();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.rightIndicator).setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case 'f':
                emptySpaceRight.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
            case 'd':
                echoDoor.setVolume(0,(volumeControl.soundFX*100)/200);
                echoDoor.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
        }
        if(levelManager.hasKey(rightPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(rightPosition).getPassCode());
            keyJingle.release();
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void playWalkingForwardLeftNoise(char leftTile, int[] leftPosition) {
        switch(leftTile){
            case 'w':
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.leftIndicator).setVisibility(View.VISIBLE);
                    }
                });
                leftSideWallTap.start();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.leftIndicator).setVisibility(View.INVISIBLE);
                    }
                });
                break;
            case 'f':
                emptySpaceLeft.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
            case 'd':
                echoDoor.setVolume((volumeControl.soundFX*100)/200,0);
                echoDoor.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
        }
        if(levelManager.hasKey(leftPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(leftPosition).getPassCode());
            keyJingle.release();
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private void playLeftofPlayer(char leftTile, int[] leftPosition) {
        switch(leftTile){
        case'e':
            passing.setVolume(volumeControl.soundFX, 0);
            passing.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case 'w':
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume(volumeControl.soundFX, 0);
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case 'f':
            emptySpaceLeft.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case 'd':
            echoDoor.setVolume(volumeControl.soundFX,0);
            echoDoor.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
        }
        if(levelManager.hasKey(leftPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(leftPosition).getPassCode());
            keyJingle.release();
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void playRightofPlayer(char rightTile, int[] rightPosition) {
        switch(rightTile){
            case'e':
            passing.setVolume(0, volumeControl.soundFX);
            passing.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case'w':
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume(0, volumeControl.soundFX);
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case'f':
            emptySpaceLeft.setVolume(0, volumeControl.soundFX);
            emptySpaceLeft.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case'd':
            echoDoor.setVolume(0,volumeControl.soundFX);
            echoDoor.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
        }
        if(levelManager.hasKey(rightPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(rightPosition).getPassCode());
            keyJingle.release();
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void playBehindPlayer(char backTile, int[] backPosition) {
        switch(backTile){
            case'e':
            passing.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);
            passing.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case'w':
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case 'f':
            emptySpaceBack.setVolume(volumeControl.soundFX, volumeControl.soundFX);
            emptySpaceBack.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case'd':
            echoDoor.setVolume((volumeControl.soundFX*100)/200,(volumeControl.soundFX*100)/200);
            echoDoor.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
        }
        if(levelManager.hasKey(backPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(backPosition).getPassCode());
            keyJingle.release();
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void playPassingSounds(int[] newPosition) {
        if (levelManager.getTileAtCoord(newPosition).getType() == 'e') {
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
            keyJingle.release();
            keyJingle = MediaPlayer.create(GameplayActivity.this, hammer);
            keyJingle.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void playEndForwardTile(char forwardTile) {
        switch (forwardTile) {
            case 'w':
                hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
                hitWall.setVolume((volumeControl.soundFX * 100) / 200, (volumeControl.soundFX * 100) / 200);
                hitWall.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
            case 'd':
            echoDoor.setVolume((volumeControl.soundFX * 100) / 200, (volumeControl.soundFX * 100) / 200);
            echoDoor.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        break;
    }
    }

    private void playEndLeftTile(char leftTile, int[] leftPosition, int volumePower) {
        switch (leftTile) {
            case'e':
                passing.setVolume((volumeControl.soundFX * 100) / 200, 0);
                passing.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
             case 'w':
                hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
                hitWall.setVolume(volumeControl.soundFX, 0);
                hitWall.start();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
             case 'f':
                emptySpaceLeft.setVolume(volumeControl.soundFX, 0);
                emptySpaceLeft.start();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
            case 'd':
                echoDoor.setVolume((volumeControl.soundFX * 100) / 400, 0);
                echoDoor.start();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            break;
        }
        if(levelManager.hasKey(leftPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(leftPosition).getPassCode());
            keyJingle.release();
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

    private void playEndRightTile(char rightTile, int[] rightPosition, int volumePower) {
        switch (rightTile) {
            case 'e':
                passing.setVolume(0, (volumeControl.soundFX * 100) / 200);
                passing.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;
            case 'w':
            hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
            hitWall.setVolume(0, volumeControl.soundFX);
            hitWall.start();
            try {
                Thread.sleep(600);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case'f':
            emptySpaceRight.setVolume(0, volumeControl.soundFX);
            emptySpaceRight.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
            case 'd':
            echoDoor.setVolume(0, (volumeControl.soundFX * 100) / 400);
            echoDoor.start();
            try {
                Thread.sleep(600);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            break;
        }
        if(levelManager.hasKey(rightPosition)){
            hammer = Uri.parse(primer + "keys0" + levelManager.getItemAtPosition(rightPosition).getPassCode());
            keyJingle.release();
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

    private void playEndingLogic() {
        ending.start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        ending.stop();
        ending.reset();
        playTutorialLogicForEnd();
        if(!tutorialLevel){
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
            levelChange = MediaPlayer.create(this, R.raw.levelchange);
            levelChange.setVolume(volumeControl.soundFX, volumeControl.soundFX);

            ending = MediaPlayer.create(GameplayActivity.this, R.raw.beatingitup);
            ending.setVolume((volumeControl.soundFX * 100) / 200, (volumeControl.soundFX * 100) / 200);
            this.nextLevel();
        }
        else{
            finish();
        }
    }

    private void playTutorialLogicForEnd() {
        switch (levelManager.getCurrentLevel()){
            case 1:
                narrator.release();
                narrator = MediaPlayer.create(this, R.raw.tutonepartfour);
                narrator.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                narrator.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                narrator.stop();
                break;
            case 3:
                narrator.release();
                narrator = MediaPlayer.create(this, R.raw.tutthreevoice);
                narrator.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                narrator.start();
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                narrator.stop();
                break;
            case 8:
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                narrator.release();
                narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tuteightendone);
                narrator.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                narrator.release();
                narrator = MediaPlayer.create(GameplayActivity.this, R.raw.none);
                //TODO MONSTER SOUND NOISE
                narrator.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                narrator.release();
                narrator = MediaPlayer.create(GameplayActivity.this, R.raw.tuteightendtwo);
                narrator.start();
                break;
        }
    }

    public void generateLevelFromConfigFile(String levelName, boolean saveGameStatus) {
        InputStream asset = null;
        FileInputStream assetTwo = null;
        levelManager.nullLevelManager();
        levelManager = null;
        levelManager = new LevelManager(this);
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
        String narration = levelManager.loadLevel(asset, assetTwo, saveGameStatus);
        if(narration != null) {
            hammer = Uri.parse(primer + narration);
            narrator.release();
            narrator = MediaPlayer.create(this, hammer);
            narrator.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
            narrator.start();
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
                             // TODO ENEMIES add enemy inside encapsulation, they are ontop of key
                        saveGame.write(')');
                    } else if(mPlayerSpawnPoint[0] == x && mPlayerSpawnPoint[1] == y){
                        saveGame.write('P');
                    }
                    else if(mMap[x][y].getType() == 'd'){
                        saveGame.write('d');
                        saveGame.write('(');
                        for(int i = 0; i < levelManager.getDoorArraySize(); i++){
                            if(levelManager.getDoor(i).getmLocation()[0] == x && levelManager.getDoor(i).getmLocation()[1] == y){
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
                    // TODO TELEPORT add something similar to key,
                     //MAKE SURE TO CHECK FOR ENEMIES AND PLAYER and encapsulate them, AKA they teleport and save
                     // or enemy is ontop of teleport
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
}
