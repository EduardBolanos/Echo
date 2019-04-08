package com.example.echo.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView onClick;
    TextView menuText;
    int currentSelect; //Counter
    int currentMenuContext; //Selected Menu
    int contextSelect; //Location in arrays
    boolean gameState = true; //new and continue
    Intent i;
    MediaPlayer mediaPlayer;
    MediaPlayer slide;
    MediaPlayer context;
    MediaPlayer beat;
    float x1, x2, y1, y2;
    //Omega String
    String omegaMenu[] = {"Start", "Instructions", "Settings", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "New Game", "Continue", "Yes", "No"};
    String omegaMenuVoice[] = {"start", "hel", "settings","soundfx","voicefx","amfx","vibration",
            "newgame", "continuegame","yes", "no"};
    int menuSize[] = {3, 4, 2, 2};
    int loc;

    // Make sure to replace package string if changed : /*IMPORTANT*/
    String primer = ("android.resource://" + "com.example.echo.echoprototype" + "/raw/");
    Uri hammer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i = new Intent(this, TestGameplay.class);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        slide = MediaPlayer.create(MainActivity.this, R.raw.slide);
        context = MediaPlayer.create(MainActivity.this,R.raw.change);
        mediaPlayer.start();
        /*IMPORTANT*/
        beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        beat.setLooping(true);
        beat.start();
        super.onCreate(savedInstanceState);
        /*IMPORTANT*/
        setContentView(R.layout.activity_main);

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        onClick = (ImageView) findViewById(R.id.click);
        currentSelect = 0;
        currentMenuContext = 0;
        contextSelect = 0;
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
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
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
                    slide.start();
                    contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
                    hammer = Uri.parse((primer + omegaMenuVoice[contextSelect + loc]));
                    mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
                    menuText.setText((String) omegaMenu[loc + contextSelect]);
                    currentSelect--;
                }
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                    slide.start();
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
                            context.start();
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
                            context.start();
                            currentMenuContext = 2;
                            currentSelect = 0;
                            contextSelect = 0;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            menuText.setText((String) omegaMenu[7]);
                            break;
                    }
                }
                else if ((y1 < y2) && (Math.abs(y1 - y2) > 400)) {
                    mediaPlayer.start();
                }
                else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))){
                    context.start();
                        switch (loc + contextSelect) {
                            case 0:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                                currentMenuContext = 2;
                                menuText.setText((String) omegaMenu[7]);
                                break;
                            case 1:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.instructions);
                                break;
                            case 2:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
                                currentMenuContext = 1;
                                menuText.setText((String) omegaMenu[3]);
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                                currentMenuContext = 3;
                                menuText.setText((String) omegaMenu[9]);
                                gameState = true;
                                break;
                            case 8:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                                currentMenuContext = 3;
                                menuText.setText((String) omegaMenu[9]);
                                gameState = false;
                                break;
                            case 9:
                                startActivity(i);
                                break;
                            case 10:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                                currentMenuContext = 2;
                                menuText.setText((String) omegaMenu[7]);
                                break;
                        }
                    }
                mediaPlayer.start();
                break;
        }
        return false;
    }

}
