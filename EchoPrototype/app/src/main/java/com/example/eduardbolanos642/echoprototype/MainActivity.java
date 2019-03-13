package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView menuText;
    int currentSelect; //Counter
    int currentMenuContext; //Selected Menu
    int contextSelect; //Location in arrays, ex
    boolean gameState = true; //new and continue
    MediaPlayer mediaPlayer;
    MediaPlayer beat;
    float x1, x2, y1, y2;
    String mainMenu[] = {"Start", "Settings"};
    String mainMenuSettings[] = {"Sound FX Volume", "Voice Volume", "Ambiance & Music", "Vibration Level"};
    String gameSelectionMenu[] = {"New Game", "Continue"};
    String mainMenuConfirmation[] = {"Yes", "No"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        mediaPlayer.start();
        beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        beat.setLooping(true);
        beat.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        currentSelect = 0;
        currentMenuContext = 0;


    }

    public boolean onTouchEvent(MotionEvent event) {
        /**
         * Cleans up Excess Audio Clips
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
                /**
                 * Gives us the ability to click on menus.
                 */
                if (x1 == x2) {
                    if ((currentMenuContext == 0)) {
                        /**
                         * Ability to click on "Start" in the main menu and then
                         * be able to see game selection menu.
                         **/
                        if ((Math.abs((currentSelect) % 2) == 0)) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            mediaPlayer.start();
                            currentSelect = 0;
                            currentMenuContext = 2;
                            menuText.setText((String) gameSelectionMenu[currentSelect]);
                        }
                        /**
                         * Ability to click on "Settings" in the main menu and then
                         * be able to see the settings that are available.
                         **/
                        else if ((Math.abs((currentSelect) % 2) == 1)) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
                            mediaPlayer.start();
                            currentSelect = 0;
                            currentMenuContext = 1;
                            menuText.setText((String) mainMenuSettings[currentSelect]);
                        }
                    }
                    else if (currentMenuContext == 2){
                        /**
                         * Ability to click on "New Game" and start a new game.
                         *
                         **/
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                        mediaPlayer.start();
                        if ((Math.abs((currentSelect) % 2) == 0)) {
                            currentSelect = 0;
                            currentMenuContext = 3;
                            menuText.setText((String) mainMenuConfirmation[currentSelect]);
                            gameState =  true;
                        }
                        /**
                         * Ability to click on "Continue",
                         * NOTE: You cannot use this option since
                         * save data is not yet implemented.
                         **/
                        else if ((Math.abs((currentSelect) % 2) == 1)) {
                            currentSelect = 0;
                            currentMenuContext = 3;
                            menuText.setText((String) mainMenuConfirmation[currentSelect]);
                            gameState = false;
                        }

                    }/**
                     * Ability to click on "Yes" that will take into the actual
                     * game.
                     **/
                    else if((currentMenuContext == 3)){
                        if((Math.abs((currentSelect) % 2) == 0)){
                            beat.stop();
                            Intent i = new Intent(MainActivity.this, TestGameplay.class);
                            startActivity(i);
                        }
                        /**
                         * Ability to click on "No" which will take you
                         * back to the game selection menu.
                         */
                        else if((Math.abs((currentSelect) % 2) == 1)){
                            currentSelect = 0;
                            currentMenuContext = 2;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            mediaPlayer.start();
                            menuText.setText((String) gameSelectionMenu[currentSelect]);
                        }
                    }
                }
                /**
                 * Ability to swipe left in between the menu options
                 * in all the cases, "Start" / "Settings", "New Game" / "Continue", etc..
                 */
                else if ((x1 < x2) && (Math.abs(x1 - x2) > 400)) {
                    //about to blow Timothy's left world
                    if (currentMenuContext == 0) {
                        contextSelect = Math.abs((currentSelect - 1) % 2);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.settings);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) mainMenu[contextSelect]);
                        currentSelect--;
                    } else if (currentMenuContext == 1) {
                        contextSelect = Math.abs((currentSelect - 1) % 4);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.voicefx);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 2) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.amfx);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 3){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.vibration);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) mainMenuSettings[contextSelect]);
                        currentSelect--;
                    } else if (currentMenuContext == 2) {
                        contextSelect = Math.abs((currentSelect - 1) % 2);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.continuegame);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) gameSelectionMenu[contextSelect]);
                        currentSelect--;
                    } else if (currentMenuContext == 3){
                        contextSelect = Math.abs((currentSelect - 1) % 2);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.no);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) mainMenuConfirmation[contextSelect]);
                        currentSelect--;
                    }
                }
                /**
                 * Ability to swipe Right in between the menu options
                 * in all the cases, "Start" / "Settings", "New Game" / "Continue", etc..
                 */
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)) {
                    //about to blow Timothy's right world
                    if (currentMenuContext == 0) {
                        contextSelect = Math.abs((currentSelect + 1) % 2);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.settings);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) mainMenu[contextSelect]);
                        currentSelect++;
                    } else if (currentMenuContext == 1) {
                        contextSelect = Math.abs((currentSelect + 1) % 4);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.voicefx);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 2) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.amfx);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 3){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.vibration);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) mainMenuSettings[contextSelect]);
                        currentSelect++;
                    } else if (currentMenuContext == 2) {
                        contextSelect = Math.abs((currentSelect + 1) % 2);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.continuegame);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) gameSelectionMenu[contextSelect]);
                        currentSelect++;
                    } else if (currentMenuContext == 3){
                        contextSelect = Math.abs((currentSelect + 1) % 2);
                        if(contextSelect == 0) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                            mediaPlayer.start();
                        }
                        else if(contextSelect == 1){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.no);
                            mediaPlayer.start();
                        }
                        menuText.setText((String) mainMenuConfirmation[contextSelect]);
                        currentSelect++;
                    }
                }
                /**
                 * Ability to swipe "up" which takes you back to the 1st menu selection.
                 */
                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    switch (currentMenuContext){
                        /**
                         * Case 0 is the main menu, there is no where to go back.
                         */
                        case 0: break;
                        /**
                         * Case 1 is the settings menu, this will take you back
                         * to the main menu.
                         */
                        case 1:
                            currentSelect = 0;
                            currentMenuContext = 0;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                            mediaPlayer.start();
                            menuText.setText((String) mainMenu[currentSelect]);
                            break;
                        /**
                         * Case 2 is the game selection menu, this will take you
                         * back to the main menu.
                         */
                        case 2:
                            currentSelect = 0;
                            currentMenuContext = 0;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                            mediaPlayer.start();
                            menuText.setText((String) mainMenu[currentSelect]);
                            break;
                        /**
                         * Case 3 is the confirmation menu, it takes you back to the
                         * game selection screen.
                         */
                        case 3:
                            currentSelect = 0;
                            currentMenuContext = 2;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            mediaPlayer.start();
                            menuText.setText((String) gameSelectionMenu[currentSelect]);
                            break;
                    }
                }

                break;

        }
        return false;
    }

}
