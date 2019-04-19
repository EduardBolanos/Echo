package com.example.echo.echoprototype;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    TextView menuText;
    SoundSettings volumeControl;
    int currentSelect; //Counter
    int currentMenuContext; //Selected Menu
    int contextSelect; //Location in arrays
    boolean gameState = true; //new and continue
    Intent gameplayActivity;
    MediaPlayer mediaPlayer;
    MediaPlayer slide;
    MediaPlayer context;
    MediaPlayer beat;


    float x1, x2, y1, y2;
    //Omega String
    String menu[] = {"Start", "Instructions", "Settings", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "Reset Volume Values", "New Game", "Continue", "Yes", "No"};
    int menuVoice[] = {R.raw.start, R.raw.hel, R.raw.settings,R.raw.soundfx,R.raw.voicefx,R.raw.amfx,R.raw.vibration, R.raw.vibration,
            R.raw.newgame, R.raw.continuegame,R.raw.yes, R.raw.no}; // TODO replace for reset
    int menuSize[] = {3, 5, 2, 2};
    int loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        gameplayActivity = new Intent(this, GameplayActivity.class);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        slide = MediaPlayer.create(MainActivity.this, R.raw.slide);
        context = MediaPlayer.create(MainActivity.this,R.raw.change);
        slide.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        context.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
        mediaPlayer.start();
        /*IMPORTANT*/
        beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        beat.setVolume(volumeControl.ambianceFX, volumeControl.ambianceFX);
        beat.setLooping(true);
        beat.start();
        super.onCreate(savedInstanceState);
        /*IMPORTANT*/
        setContentView(R.layout.activity_main);

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        currentSelect = 0;
        currentMenuContext = 0;
        contextSelect = 0;
        menuText.setText((String) menu[0]);


    }
    @Override
    protected void onPause(){
        beat.stop();
        FileOutputStream saveSettings;
        String parser;
        try {
            saveSettings = openFileOutput("settings", this.MODE_PRIVATE);
            parser = Float.toString(volumeControl.soundFX);
            saveSettings.write(parser.charAt(0));
            saveSettings.write(parser.charAt(1));
            saveSettings.write(parser.charAt(2));
            saveSettings.write('#');
            parser = Float.toString(volumeControl.voiceFX);
            saveSettings.write(parser.charAt(0));
            saveSettings.write(parser.charAt(1));
            saveSettings.write(parser.charAt(2));
            saveSettings.write('#');
            parser = Float.toString(volumeControl.ambianceFX);
            saveSettings.write(parser.charAt(0));
            saveSettings.write(parser.charAt(1));
            saveSettings.write(parser.charAt(2));
            saveSettings.write('#');
            parser = Float.toString(volumeControl.vibrationIntensity);
            saveSettings.write(parser.charAt(0));
            saveSettings.write(parser.charAt(1));
            saveSettings.write(parser.charAt(2));
            saveSettings.write('#');
            saveSettings.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();

    }
    @Override
    protected void onResume(){
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
        beat.setVolume(volumeControl.ambianceFX, volumeControl.ambianceFX);
        beat.start();
        currentMenuContext = 0;
        currentSelect = 0;
        contextSelect = 0;
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
        mediaPlayer.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        mediaPlayer.start();
        menuText.setText((String) menu[0]);
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
                    mediaPlayer.release();
                    slide.start();
                    contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
                    mediaPlayer = MediaPlayer.create(MainActivity.this, menuVoice[contextSelect + loc]);
                    mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                    menuText.setText((String) menu[loc + contextSelect]);
                    currentSelect--;
                }
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                    mediaPlayer.release();
                    slide.start();
                    contextSelect = Math.abs((currentSelect + 1) % menuSize[currentMenuContext]);
                    mediaPlayer = MediaPlayer.create(MainActivity.this, menuVoice[contextSelect + loc]);
                    mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                    menuText.setText((String) menu[loc + contextSelect]);
                    currentSelect++;

                }
                else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    mediaPlayer.release();
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
                            mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                            menuText.setText((String) menu[0]);
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
                            mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                            menuText.setText((String) menu[7]);
                            break;
                    }
                }
                else if ((y1 < y2) && (Math.abs(y1 - y2) > 400)) {
                    mediaPlayer.start();
                }
                else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))){
                    mediaPlayer.release();
                    context.start();
                        switch (loc + contextSelect) {
                            case 0:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                                currentMenuContext = 2;
                                contextSelect = 0;
                                menuText.setText((String) menu[7]);
                                break;
                            case 1:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.instructions);
                                break;
                            case 2:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundfx);
                                currentMenuContext = 1;
                                contextSelect = 0;
                                menuText.setText((String) menu[3]);
                                break;
                            case 3:
                                mediaPlayer = mediaPlayer.create(MainActivity.this, R.raw.change);
                                if(volumeControl.soundFX == 0.3f){
                                    volumeControl.soundFX = 0.6f;
                                } else if(volumeControl.soundFX == 0.6f){
                                    volumeControl.soundFX = 1.0f;
                                } else if(volumeControl.soundFX == 1.0){
                                    volumeControl.soundFX = 0.3f;
                                }
                                slide.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                context.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                break;
                            case 4:
                                mediaPlayer = mediaPlayer.create(MainActivity.this, R.raw.change);
                                if(volumeControl.voiceFX == 0.3f){
                                    volumeControl.voiceFX = 0.6f;
                                } else if(volumeControl.voiceFX == 0.6f){
                                    volumeControl.voiceFX = 1.0f;
                                } else if(volumeControl.voiceFX == 1.0f){
                                    volumeControl.voiceFX = 0.3f;
                                }
                                break;
                            case 5:
                                mediaPlayer = mediaPlayer.create(MainActivity.this, R.raw.change);
                                if(volumeControl.ambianceFX == 0.3f){
                                    volumeControl.ambianceFX = 0.6f;
                                } else if(volumeControl.ambianceFX == 0.6f){
                                    volumeControl.ambianceFX = 1.0f;
                                } else if(volumeControl.ambianceFX == 1.0f){
                                    volumeControl.ambianceFX = 0.3f;
                                }
                                beat.stop();
                                beat.setVolume(volumeControl.ambianceFX, volumeControl.ambianceFX);
                                beat.start();
                                break;
                            case 6:
                                mediaPlayer = mediaPlayer.create(MainActivity.this, R.raw.change);
                                if(volumeControl.vibrationIntensity == 0.3f){
                                    volumeControl.vibrationIntensity = 0.6f;
                                } else if(volumeControl.vibrationIntensity == 0.6f){
                                    volumeControl.vibrationIntensity = 1.0f;
                                } else if(volumeControl.vibrationIntensity == 1.0f){
                                    volumeControl.vibrationIntensity = 0.3f;
                                }
                                break;
                            case 7:
                                mediaPlayer = mediaPlayer.create(MainActivity.this, R.raw.change);
                                volumeControl.soundFX = 0.6f;
                                volumeControl.voiceFX = 1.0f;
                                volumeControl.ambianceFX = 0.3f;
                                volumeControl.vibrationIntensity = 0.6f;
                                slide.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                context.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                beat.stop();
                                beat.setVolume(volumeControl.ambianceFX, volumeControl.ambianceFX);
                                beat.start();
                                break;
                            case 8:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                                currentMenuContext = 3;
                                contextSelect = 0;
                                menuText.setText((String) menu[9]);
                                gameState = true;
                                break;
                            case 9:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.yes);
                                currentMenuContext = 3;
                                contextSelect = 0;
                                menuText.setText((String) menu[9]);
                                gameState = false;
                                break;
                            case 10:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.change);
                                startActivity(gameplayActivity);
                                break;
                            case 11:
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.newgame);
                                currentMenuContext = 2;
                                menuText.setText((String) menu[7]);
                                break;
                        }
                    }
                mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                mediaPlayer.start();
                break;
        }
        return false;
    }

}
