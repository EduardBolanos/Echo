package com.example.eduardbolanos642.echoprototype;

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
    int contextSelect; //Location in arrays, ex
    boolean gameState = true; //new and continue
    MediaPlayer mediaPlayer;
    MediaPlayer beat;
    float x1, x2, y1, y2;
    // menuString
    String mainMenu[] = {"Start", "Settings"};
    String mainMenuVoice[] = {"start", "settings"};
    //settingString
    String mainMenuSettings[] = {"Sound FX Volume", "Voice Volume", "Ambiance & Music", "Vibration Level"};
    String mainMenuSettingsVoice[] = {"soundfx","voicefx","amfx","vibration"};
    //selectionString
    String gameSelectionMenu[] = {"New Game", "Continue"};
    String gameSelectionMenuVoice[] = {"newgame", "continuegame"};
    //yesno
    String mainMenuConfirmation[] = {"Yes", "No"};
    String mainMenuConfirmationVoice[] = {"yes", "no"};
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
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        mediaPlayer.start();
        //beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        //beat.setLooping(true);
        //beat.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        currentSelect = 0;
        currentMenuContext = 0;
        menuText.setText((String) omegaMenu[0]);


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
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                            menuText.setText((String) omegaMenu[0]);
                            break;
                        /**
                         * Case 3 is the confirmation menu, it takes you back to the
                         * game selection screen.
                         */
                        case 3:
                            currentMenuContext = 2;
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            menuText.setText((String) omegaMenu[6]);
                            break;
                    }
                    currentSelect = 0;
                    contextSelect = 0;
                }
                else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))
                        || ((y1 > y2) && (Math.abs(y1 - y2) > 400))){
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
                        case 2:break;
                        case 3:break;
                        case 4:break;
                        case 5:break;
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
                              // beat.stop();
                              // beat.release();
                                Intent i = new Intent(MainActivity.this, TestGameplay.class);
                                startActivity(i);
                            break;
                        case 9:
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                            currentMenuContext = 2;
                            menuText.setText((String) omegaMenu[6]);
                            break;
                    }
                    currentSelect = 0;
                    }
                mediaPlayer.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                break;

                /**
                 * Ability to swipe left in between the menu options
                 * in all the cases, "Start" / "Settings", "New Game" / "Continue", etc..
                 */
//                if ((x1 < x2) && (Math.abs(x1 - x2) > 400)) {
//                    //about to blow Timothy's left world
//                    switch(currentMenuContext){
//                        case 0:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse((primer + mainMenuVoice[contextSelect]));
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) mainMenu[contextSelect]);
//                            currentSelect--;
//                            break;
//                        case 1:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse(primer + mainMenuSettingsVoice[contextSelect]);
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) mainMenuSettings[contextSelect]);
//                            currentSelect--;
//                            break;
//                        case 2:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse(primer + gameSelectionMenuVoice[contextSelect]);
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) gameSelectionMenu[contextSelect]);
//                            currentSelect--;
//                            break;
//                        case 3:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse(primer + mainMenuConfirmationVoice[contextSelect]);
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) mainMenuConfirmation[contextSelect]);
//                            currentSelect--;
//                            break;
//                        }
//                    }
//                /**
//                 * Ability to swipe Right in between the menu options
//                 * in all the cases, "Start" / "Settings", "New Game" / "Continue", etc..
//                 */
//                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)) {
//                    //about to blow Timothy's right world
//                    switch(currentMenuContext){
//                        case 0:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse((primer + mainMenuVoice[contextSelect]));
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) mainMenu[contextSelect]);
//                            currentSelect++;
//                            break;
//                        case 1:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse(primer + mainMenuSettingsVoice[contextSelect]);
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) mainMenuSettings[contextSelect]);
//                            currentSelect++;
//                            break;
//                        case 2:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse(primer + gameSelectionMenuVoice[contextSelect]);
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) gameSelectionMenu[contextSelect]);
//                            currentSelect++;
//                            break;
//                        case 3:
//                            contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
//                            hammer = Uri.parse(primer + mainMenuConfirmationVoice[contextSelect]);
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, hammer);
//                            mediaPlayer.start();
//                            mediaPlayer.release();
//                            menuText.setText((String) mainMenuConfirmation[contextSelect]);
//                            currentSelect++;
//                            break;
//                    }
//                }
//                /**
//                 * Ability to swipe "up" which takes you back to the 1st menu selection.
//                 */
//                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
//                    switch (currentMenuContext){
//                        /**
//                         * Case 0 is the main menu, there is no where to go back.
//                         */
//                        case 0: break;
//                        /**
//                         * Case 1 is the settings menu, this will take you back
//                         * to the main menu.
//                         */
//                        case 1:
//                            currentSelect = 0;
//                            currentMenuContext = 0;
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
//                            mediaPlayer.start();
//                            menuText.setText((String) mainMenu[currentSelect]);
//                            break;
//                        /**
//                         * Case 2 is the game selection menu, this will take you
//                         * back to the main menu.
//                         */
//                        case 2:
//                            currentSelect = 0;
//                            currentMenuContext = 0;
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
//                            mediaPlayer.start();
//                            menuText.setText((String) mainMenu[currentSelect]);
//                            break;
//                        /**
//                         * Case 3 is the confirmation menu, it takes you back to the
//                         * game selection screen.
//                         */
//                        case 3:
//                            currentSelect = 0;
//                            currentMenuContext = 2;
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
//                            mediaPlayer.start();
//                            menuText.setText((String) gameSelectionMenu[currentSelect]);
//                            break;
//                    }
//                }
//                /**
//                 * Gives us the ability to click on menus.
//                 */
//                else if ((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50)) {
//                    if ((currentMenuContext == 0)) {
//                        /**
//                         * Ability to click on "Start" in the main menu and then
//                         * be able to see game selection menu.
//                         **/
//                        if ((Math.abs((currentSelect) % 2) == 0)) {
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
//                            mediaPlayer.start();
//                            currentSelect = 0;
//                            currentMenuContext = 2;
//                            menuText.setText((String) gameSelectionMenu[currentSelect]);
//                        }
//                        /**
//                         * Ability to click on "Settings" in the main menu and then
//                         * be able to see the settings that are available.
//                         **/
//                        else if ((Math.abs((currentSelect) % 2) == 1)) {
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
//                            mediaPlayer.start();
//                            currentSelect = 0;
//                            currentMenuContext = 1;
//                            menuText.setText((String) mainMenuSettings[currentSelect]);
//                        }
//                    }
//                    else if (currentMenuContext == 2){
//                        /**
//                         * Ability to click on "New Game" and start a new game.
//                         *
//                         **/
//                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
//                        mediaPlayer.start();
//                        if ((Math.abs((currentSelect) % 2) == 0)) {
//                            currentSelect = 0;
//                            currentMenuContext = 3;
//                            menuText.setText((String) mainMenuConfirmation[currentSelect]);
//                            gameState =  true;
//                        }
//                        /**
//                         * Ability to click on "Continue",
//                         * NOTE: You cannot use this option since
//                         * save data is not yet implemented.
//                         **/
//                        else if ((Math.abs((currentSelect) % 2) == 1)) {
//                            currentSelect = 0;
//                            currentMenuContext = 3;
//                            menuText.setText((String) mainMenuConfirmation[currentSelect]);
//                            gameState = false;
//                        }
//
//                    }/**
//                     * Ability to click on "Yes" that will take into the actual
//                     * game.
//                     **/
//                    else if((currentMenuContext == 3)){
//                        if((Math.abs((currentSelect) % 2) == 0)){
//                            beat.stop();
//                            Intent i = new Intent(MainActivity.this, TestGameplay.class);
//                            startActivity(i);
//                        }
//                        /**
//                         * Ability to click on "No" which will take you
//                         * back to the game selection menu.
//                         */
//                        else if((Math.abs((currentSelect) % 2) == 1)){
//                            currentSelect = 0;
//                            currentMenuContext = 2;
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
//                            mediaPlayer.start();
//                            menuText.setText((String) gameSelectionMenu[currentSelect]);
//                        }
//                    }
//                }
//                break;
//            ***/
        }
        return false;
    }

}
