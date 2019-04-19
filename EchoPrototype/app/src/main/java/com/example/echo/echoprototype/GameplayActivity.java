package com.example.echo.echoprototype;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class GameplayActivity extends AppCompatActivity {

   private LevelManager levelManager;
   private SoundSettings volumeControl;
   private MediaPlayer turn;
   private MediaPlayer moveForward;
   private MediaPlayer ending;
   private MediaPlayer hitWall;
   private MediaPlayer leftSideWallTap;
   private MediaPlayer rightSideWallTap;
   private MediaPlayer[] echo;
   private MediaPlayer emptySpaceLeft;
   private MediaPlayer emptySpaceRight;
   private MediaPlayer emptySpaceBack;
   private MediaPlayer passing;
   private Player player;
    float initialInputCoordinate_X, initialInputCoordinate_Y, finalInputCoordinate_X, finalInputCoordinate_Y;
    Intent navigateToInGameMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
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
        turn = MediaPlayer.create(GameplayActivity.this, R.raw.swooshleft); // TODO change with swoosh

        moveForward = MediaPlayer.create(GameplayActivity.this, R.raw.genericfootsteps);
        moveForward.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        ending = MediaPlayer.create(GameplayActivity.this, R.raw.my_jam);
        ending.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        hitWall = MediaPlayer.create(GameplayActivity.this, R.raw.wall_collision);
        hitWall.setVolume(volumeControl.soundFX, volumeControl.soundFX);

        passing = MediaPlayer.create(GameplayActivity.this, R.raw.goalresponse);
        passing.setVolume(volumeControl.soundFX, volumeControl.soundFX);

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

        MediaPlayer leftSideWallTap = MediaPlayer.create(GameplayActivity.this, R.raw.echolocate); // TODO TIP
        MediaPlayer rightSideWallTap = MediaPlayer.create(GameplayActivity.this, R.raw.echolocate);; // TODO TAP

        MediaPlayer emptySpaceLeft = MediaPlayer.create(GameplayActivity.this, R.raw.swooshleft);;
        MediaPlayer emptySpaceRight = MediaPlayer.create(GameplayActivity.this, R.raw.swooshright);

        levelManager = LevelManager.get(this);
        player = new Player(this);
        String newGameState = getIntent().getExtras().getString("gameState");
        if(newGameState.equals("yes")){
        generateLevelFromConfigFile("level1.txt", false);
        }
        else if(newGameState.equals("no")){
            generateLevelFromConfigFile("saveGame", true);
        }
        player.setOrientation(levelManager.getPlayerSpawnOrientation());
        player.setPosition(levelManager.getPlayerSpawnPosition());
        navigateToInGameMenu = new Intent(this, InGameMenu.class);

        if (savedInstanceState != null)
        {
            //player.setPosition();
            //player.setOrientation();
        }
    }

    @Override
    protected void onPause(){
        this.saveGame();
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
            }
            //if tile is item get item, if tile is end play end
        }
        else {
            //play wall hit
            hitWall.start();
            //if(Map.isLegal(newPosition)) == false  dont move forward play tileSound
        }
    }

    public void echolocate() {
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
            // TODO play goal sound near
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (leftTile == 'w') {
            // TODO play wall left sound near
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (leftTile == 'f') {
            // TODO play wind sound near
            //stop playing leftSound
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        rightTile = levelManager.getTileAtCoord(rightPosition).getType(); // What is near the players' right side?
        if (rightTile == 'e') {
            // TODO play goal sound near
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (rightTile == 'w') {
            // TODO play wall sound near
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (rightTile == 'f') {
            // TODO play floor sound near
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            //stop playing rightSound
        }
        char backTile = levelManager.getTileAtCoord(backPosition).getType(); // what is near the player's back?
        if (backTile == 'e') {
            // TODO play goal sound near
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (backTile == 'w') {
            // TODO play wall sound near
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        } else if (backTile == 'f') {
            // TODO play floor sound near
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            //stop playing rightSound
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
                    // TODO play goal sound
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'w') {
                    // TODO play wall left sound
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'f') {
                    // TODO play wind sound
                    //stop playing leftSound
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
                rightPosition = moveFromPosition(rightOrientation, newPosition);
                rightTile = levelManager.getTileAtCoord(rightPosition).getType(); // checks right area
                if (rightTile == 'e') {
                    // TODO play goal sound
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'w') {
                    // TODO play wall sound
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'f') {
                    // TODO play floor sound
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    //stop playing rightSound
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
                passing.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                passing.stop();
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
            //play wall
            hitWall.setVolume((volumeControl.soundFX*100)/200, (volumeControl.soundFX*100)/200);
            // 1/2 volume for forward wall
            hitWall.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if(playOnce == 1) {
                //go back to legal space
                newPosition = moveFromPosition(backOrientation, newPosition);
                //play sounds to identify left
                leftPosition = moveFromPosition(leftOrientation, newPosition);
                leftTile = levelManager.getTileAtCoord(leftPosition).getType();
                if (leftTile == 'e') {
                    // TODO play goal sound
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'w') {
                    // TODO play wall left sound
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (leftTile == 'f') {
                    // TODO play wind sound
                    //stop playing leftSound
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                //play sounds to identify right
                rightPosition = moveFromPosition(rightOrientation, newPosition);
                rightTile = levelManager.getTileAtCoord(rightPosition).getType();
                if (rightTile == 'e') {
                    // TODO play goal sound
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'w') {
                    // TODO play wall sound
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else if (rightTile == 'f') {
                    // TODO play floor sound
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    //stop playing rightSound
                }
            }
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

    public void generateLevelFromConfigFile(String levelName, boolean saveGame) {
        // logic from level.loadLevel() should be transferred here.
        // save determines if in asset or file
        int data = 0;
        String concatinator = "";
        int state = 0;
        int locX = 0;
        int locY = 0;
        int bypass = 0;
        Tile map[][] = null;
        InputStream asset = null;
        FileInputStream assetTwo = null;

        if(!saveGame) {
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
                if(!saveGame) {
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
                                    case 102:
                                        map[locX][locY] = levelManager.floor;
                                        break;
                                    case 101:
                                        map[locX][locY] = levelManager.end;
                                        int[] ep = {locX, locY};
                                        levelManager.setPlayerSpawnPosition(ep);
                                        break;
                                    case 119:
                                        map[locX][locY] = levelManager.wall;
                                        break;
                                    case 80:
                                        map[locX][locY] = levelManager.floor;
                                        int[] playerPos = {locX, locY};
                                        levelManager.setPlayerSpawnPosition(playerPos);
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
                            asset = null;
                            Runtime r = Runtime.getRuntime();
                            r.gc();
                            data = -1;

                            break;
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
             for(int y = levelManager.sizeY - 1; y >= 0 ; y--){
                for(int x = 0; x < levelManager.sizeX; x++){
                    // if(mEndPoint[0] == x && mEndPoint[1] == y){
                    //     saveGame.write(levelManager.end.getType());
                   //  }
                     //else
                         if(mPlayerSpawnPoint[0] == x && mPlayerSpawnPoint[1] == y){
                         saveGame.write('P');
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
             saveGame.close();
         } catch (Exception e) {
        e.printStackTrace();
        }
     }

}
