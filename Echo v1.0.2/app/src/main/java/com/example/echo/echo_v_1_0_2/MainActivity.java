package com.example.echo.echo_v_1_0_2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    TextView menuText;
    private SoundSettings volumeControl;
    public String primer = ("android.resource://" + "com.example.echo.echo_v_1_0_2" + "/raw/"); // Incase things happen such as package reference for public
    private Uri hammer;
    private int currentSelect; //Counter
    private int currentMenuContext; //Selected Menu
    int contextSelect; //Location in arrays
    boolean gameState = true; //new and continue
    Intent gameplayActivity;
    private MediaPlayer beat;


    float x1, x2, y1, y2;
    //Omega String
    private String menu[] = {"Start", "Instructions", "Settings", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "Reset Volume Values", "New Game", "Continue", "Yes", "No"};
    private String menuVoice[] = {"start", "hel", "settings", "soundfx", "voicefx", "amfx", "vibration", "reset",
            "newgame", "continuegame", "yes", "no"};
    private int menuSize[] = {3, 5, 2, 2};
    private int loc;

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
        playSoundScape(R.raw.intro, volumeControl.soundFX, volumeControl.soundFX);
        /*IMPORTANT*/
        beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        beat.setVolume((volumeControl.ambianceFX * 100) / 200, (volumeControl.ambianceFX * 100) / 200);
        beat.setLooping(true);
        beat.start();
        super.onCreate(savedInstanceState);
        /*IMPORTANT*/
        setContentView(R.layout.activity_main);
        Runtime r = Runtime.getRuntime();
        r.gc();

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        currentSelect = 0;
        currentMenuContext = 0;
        contextSelect = 0;
        menuText.setText((String) menu[0]);


    }

    @Override
    protected void onPause() {
        beat.stop();
        beat.release();
        this.saveSettings();
        super.onPause();

    }

    @Override
    protected void onResume() {
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
        beat = MediaPlayer.create(MainActivity.this, R.raw.beatingitup); //We needed this song in our app
        beat.setVolume((volumeControl.ambianceFX * 100) / 200, (volumeControl.ambianceFX * 100) / 200);
        beat.setLooping(true);
        beat.start();
        currentMenuContext = 0;
        currentSelect = 0;
        contextSelect = 0;
        menuText.setText((String) menu[0]);
        Runtime r = Runtime.getRuntime();
        r.gc();
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
                for (int x = 0; x < currentMenuContext; x++) {
                    loc = (loc + menuSize[x]);
                }
                if ((x1 < x2) && (Math.abs(x1 - x2) > 400)) {
                    playSoundScape(R.raw.slide, volumeControl.soundFX, volumeControl.soundFX);
                    contextSelect = Math.abs((currentSelect - 1) % menuSize[currentMenuContext]);
                    playByUriSoundScape(menuVoice[contextSelect + loc], volumeControl.voiceFX, volumeControl.voiceFX);
                    menuText.setText((String) menu[loc + contextSelect]);
                    currentSelect--;
                } else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)) {
                    playSoundScape(R.raw.slide, volumeControl.soundFX, volumeControl.soundFX);
                    contextSelect = Math.abs((currentSelect + 1) % menuSize[currentMenuContext]);
                    playByUriSoundScape(menuVoice[contextSelect + loc], volumeControl.voiceFX, volumeControl.voiceFX);
                    menuText.setText((String) menu[loc + contextSelect]);
                    currentSelect++;

                } else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    switch (currentMenuContext) {
                        /**
                         * Case 0 is the main menu, there is no where to go back.
                         */
                        case 0:
                            break;
                        /**
                         * Case 1 is the settings menu and Case 2 is the game selection menu, this will take you back
                         * to the main menu.
                         */
                        case 1:
                        case 2:
                            playSoundScape(R.raw.change, volumeControl.soundFX, volumeControl.soundFX);
                            currentMenuContext = 0;
                            currentSelect = 0;
                            contextSelect = 0;
                            playSoundScape(R.raw.start, volumeControl.voiceFX, volumeControl.voiceFX);
                            menuText.setText((String) menu[0]);
                            break;
                        /**
                         * Case 3 is the confirmation menu, it takes you back to the
                         * game selection screen.
                         */
                        case 3:
                            playSoundScape(R.raw.change, volumeControl.soundFX, volumeControl.soundFX);
                            currentMenuContext = 2;
                            currentSelect = 0;
                            contextSelect = 0;
                            playSoundScape(R.raw.newgame, volumeControl.voiceFX, volumeControl.voiceFX);
                            menuText.setText((String) menu[8]);
                            break;
                    }
                } else if ((y1 < y2) && (Math.abs(y1 - y2) > 400)) {
                    playByUriSoundScape(menuVoice[contextSelect + loc], volumeControl.voiceFX, volumeControl.voiceFX);
                } else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))) {
                    playSoundScape(R.raw.change, volumeControl.soundFX, volumeControl.soundFX);
                    switch (loc + contextSelect) {
                        case 0:
                            playSoundScape(R.raw.newgame, volumeControl.soundFX, volumeControl.soundFX);
                            currentMenuContext = 2;
                            contextSelect = 0;
                            menuText.setText((String) menu[8]);
                            break;
                        case 1:
                            playSoundScape(R.raw.instructions, volumeControl.voiceFX, volumeControl.voiceFX);
                            break;
                        case 2:
                            playSoundScape(R.raw.soundfx, volumeControl.soundFX, volumeControl.soundFX);
                            currentMenuContext = 1;
                            contextSelect = 0;
                            menuText.setText((String) menu[3]);
                            break;
                        case 3:
                            if (volumeControl.soundFX == 0.3f) {
                                volumeControl.soundFX = 0.6f;
                            } else if (volumeControl.soundFX == 0.6f) {
                                volumeControl.soundFX = 1.0f;
                            } else if (volumeControl.soundFX == 1.0) {
                                volumeControl.soundFX = 0.3f;
                            }
                            playSoundScape(R.raw.soundfx, volumeControl.soundFX, volumeControl.soundFX);
                            break;
                        case 4:
                            if (volumeControl.voiceFX == 0.3f) {
                                volumeControl.voiceFX = 0.6f;
                            } else if (volumeControl.voiceFX == 0.6f) {
                                volumeControl.voiceFX = 1.0f;
                            } else if (volumeControl.voiceFX == 1.0f) {
                                volumeControl.voiceFX = 0.3f;
                            }
                            playSoundScape(R.raw.voicefx, volumeControl.voiceFX, volumeControl.voiceFX);
                            break;
                        case 5:
                            if (volumeControl.ambianceFX == 0.3f) {
                                volumeControl.ambianceFX = 0.6f;
                                ;
                            } else if (volumeControl.ambianceFX == 0.6f) {
                                volumeControl.ambianceFX = 1.0f;
                            } else if (volumeControl.ambianceFX == 1.0f) {
                                volumeControl.ambianceFX = 0.3f;
                            }
                            playSoundScape(R.raw.amfx, volumeControl.ambianceFX, volumeControl.ambianceFX);
                            beat.setVolume((volumeControl.ambianceFX * 100) / 200, (volumeControl.ambianceFX * 100) / 200);
                            beat.start();
                            break;
                        case 6:
                            if (volumeControl.vibrationIntensity == 0.3f) {
                                volumeControl.vibrationIntensity = 0.6f;
                            } else if (volumeControl.vibrationIntensity == 0.6f) {
                                volumeControl.vibrationIntensity = 1.0f;
                            } else if (volumeControl.vibrationIntensity == 1.0f) {
                                volumeControl.vibrationIntensity = 0.3f;
                            }
                            playSoundScape(R.raw.slide, volumeControl.vibrationIntensity, volumeControl.vibrationIntensity);
                            break;
                        case 7:
                            volumeControl.soundFX = 0.6f;
                            volumeControl.voiceFX = 1.0f;
                            volumeControl.ambianceFX = 0.3f;
                            volumeControl.vibrationIntensity = 0.6f;
                            beat.setVolume((volumeControl.ambianceFX * 100) / 200, (volumeControl.ambianceFX * 100) / 200);
                            beat.start();
                            playSoundScape(R.raw.reset, volumeControl.voiceFX, volumeControl.voiceFX);
                            break;
                        case 8:
                            playSoundScape(R.raw.yes, volumeControl.voiceFX, volumeControl.voiceFX);
                            currentMenuContext = 3;
                            currentSelect = 0;
                            contextSelect = 0;
                            menuText.setText((String) menu[10]);
                            gameState = true;
                            break;
                        case 9:
                            try {
                                openFileInput("saveGame");
                                playSoundScape(R.raw.yes, volumeControl.voiceFX, volumeControl.voiceFX);
                                currentMenuContext = 3;
                                contextSelect = 0;
                                currentSelect = 0;
                                menuText.setText((String) menu[10]);
                                gameState = false;
                            } catch (Exception e) {
                                playSoundScape(R.raw.continueerror, volumeControl.voiceFX, volumeControl.voiceFX);
                            }
                            break;
                        case 10:
                            playSoundScape(R.raw.change, volumeControl.soundFX, volumeControl.soundFX);
                            if (gameState) {
                                gameplayActivity.putExtra("gameState", "yes");
                            } else {
                                gameplayActivity.putExtra("gameState", "no");
                            }
                            startActivity(gameplayActivity);
                            break;
                        case 11:
                            playSoundScape(R.raw.newgame, volumeControl.voiceFX, volumeControl.voiceFX);
                            currentMenuContext = 2;
                            currentSelect = 0;
                            contextSelect = 0;
                            menuText.setText((String) menu[8]);
                            break;
                    }
                }
                break;
        }
        return false;
    }

    public void saveSettings() {
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
    }

    void play(int resourceID) {

        final MediaPlayer player = MediaPlayer.create(this, resourceID);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }

    void playByUri(String soundName) {

        hammer = Uri.parse(primer + soundName);
        final MediaPlayer player = MediaPlayer.create(this, hammer);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }

    void playSoundScape(int resourceID, float left, float right) {


        final MediaPlayer player = MediaPlayer.create(this, resourceID);
        player.setVolume(left, right);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }

    void playByUriSoundScape(String soundName, float left, float right) {

        hammer = Uri.parse(primer + soundName);
        final MediaPlayer player = MediaPlayer.create(this, hammer);
        player.setVolume(left, right);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }
}
