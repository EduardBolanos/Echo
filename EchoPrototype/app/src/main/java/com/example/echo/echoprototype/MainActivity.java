package com.example.echo.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView menuText;
    int currentSelect; //Counter
    int currentMenuContext; //Selected Menu
    int contextSelect; //Location in arrays
    boolean gameState = true; //new and continue

    Intent i;
    MediaPlayer mediaPlayer;
    MediaPlayer beat;
    float x1, x2, y1, y2;
    //Omega String
    String omegaMenu[] = {"Start", "Settings", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "New Game", "Continue", "Yes", "No"};
    String omegaMenuVoice[] = {"start", "settings","soundfx","voicefx","amfx","vibration",
            "newgame", "continuegame","yes", "no"};
    int menuSize[] = {2, 4, 2, 2};
    int loc;

    // Make sure to replace package string if changed : /*IMPORTANT*/
    String primer = ("android.resource://" + "com.example.eduardbolanos642.echoprototype" + "/raw/");
    Uri hammer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i = new Intent(this, InGameMenu.class);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        mediaPlayer.start();
        /*IMPORTANT*/
        beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        beat.setLooping(true);
        beat.start();

        super.onCreate(savedInstanceState);
        /*IMPORTANT*/
        setContentView(R.layout.activity_main); // RE-ADD VISUAL INSTRUCTIONS TO THE MENU, Like Arrows.

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        currentSelect = 0;
        currentMenuContext = 0;
        menuText.setText((String) omegaMenu[0]);


    }
    @Override
    protected void onPause(){
        beat.stop();
        super.onPause();

    }
    @Override
    protected void onResume(){
        beat.start();
        currentMenuContext = 0;
        currentSelect = 0;
        contextSelect = 0;
        menuText.setText((String) omegaMenu[0]);
        super.onResume();

    }

    public boolean onTouchEvent(MotionEvent event) {
        /**
         * Cleans up Excess Audio Clips - if not released
         */
        Runtime r = Runtime.getRuntime();
        r.gc();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                //OMEGA CODE BLOCK
                loc = 0;
                for(int x = 0; x < currentMenuContext; x++){
                    loc = (loc + menuSize[x]);
                }
                if ((x1 < x2) && (Math.abs(x1 - x2) > 400)) {
                    contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
                    hammer = Uri.parse((primer + omegaMenuVoice[contextSelect + loc]));
                    mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
                    menuText.setText((String) omegaMenu[loc + contextSelect]);
                    currentSelect--;
                }
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                    contextSelect = Math.abs((currentSelect + 1) % menuSize[currentMenuContext]);
                    hammer = Uri.parse((primer + omegaMenuVoice[contextSelect + loc]));
                    mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
                    menuText.setText((String) omegaMenu[loc + contextSelect]);
                    currentSelect++;

                }
                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    switch (currentMenuContext){
                        /**
                         * Case 0 is the main menu, there is no where to go back.
                         */
                        case 0: break;
                        /**
                         * Case 1 is the settings menu and Case 2 is the game selection menu, this will take you back
                         * to the main menu.
                         */
                        case 1:
                        case 2:
                            currentMenuContext = 0;
                            currentSelect = 0;
                            contextSelect = 0;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                            menuText.setText((String) omegaMenu[0]);
                            break;
                        /**
                         * Case 3 is the confirmation menu, it takes you back to the
                         * game selection screen.
                         */
                        case 3:
                            currentMenuContext = 2;
                            currentSelect = 0;
                            contextSelect = 0;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            menuText.setText((String) omegaMenu[6]);
                            break;
                    }
                }
                else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))){
                        switch (loc + contextSelect) {
                            case 0:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                                currentMenuContext = 2;
                                menuText.setText((String) omegaMenu[6]);
                                break;
                            case 1:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
                                currentMenuContext = 1;
                                menuText.setText((String) omegaMenu[2]);
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                                currentMenuContext = 3;
                                menuText.setText((String) omegaMenu[8]);
                                gameState = true;
                                break;
                            case 7:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                                currentMenuContext = 3;
                                menuText.setText((String) omegaMenu[8]);
                                gameState = false;
                                break;
                            case 8:
                                startActivity(i);
                                break;
                            case 9:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                                currentMenuContext = 2;
                                menuText.setText((String) omegaMenu[6]);
                                break;
                        }
                    }
                mediaPlayer.start();
                break;
        }
        return false;
    }

}
