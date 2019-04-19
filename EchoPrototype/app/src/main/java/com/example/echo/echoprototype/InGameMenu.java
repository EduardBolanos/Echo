package com.example.echo.echoprototype;

import android.media.MediaPlayer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class InGameMenu extends AppCompatActivity {
    int currentSelectIG; //Counter
    int currentMenuContextIG; //Selected Menu
    int contextSelectIG; //Location in arrays, ex
    //Omega String
    String omegaMenu[] = {"Inventory", "Settings and Navigation", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "Reset Volume Values", "Return to Menu", "Instructions"};
    // create these voice files
    int omegaMenuVoice[] = {R.raw.inventory, R.raw.explain, R.raw.soundfx, R.raw.voicefx, R.raw.amfx,
            R.raw.vibration, R.raw.vibration, R.raw.returntomenu ,R.raw.help}; // TODO get sound for reset
    int menuSize[] = {2, 7};
    String primer = ("android.resource://" + "com.example.echo.echoprototype" + "/raw/");
    Uri hammer;
    int loc;
    MediaPlayer mediaPlayer;
    MediaPlayer ambiance; // WE ARE APPARENTLY BRITISH DEVELOPERS
    int nodeSize;
    ItemNode fatherNode;
    ItemNode childNode;
    ItemNode currentNode;
    float x1, x2, y1, y2;
    TextView menuText;
    MediaPlayer slide;
    MediaPlayer context;
    SoundSettings volumeControl;
    /**
     * TEST: Use this code for only as a last resort
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ingamemenu); // CHANGE MENU ACTIVITY TO NEW ONE , BLACK BACKGROUND, WHITE TEXT /*IMPORTANT*/

        //gives memes /*IMPORTANT*/
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
        slide = MediaPlayer.create(InGameMenu.this, R.raw.slide);
        context = MediaPlayer.create(InGameMenu.this,R.raw.change);
        slide.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        context.setVolume(volumeControl.soundFX, volumeControl.soundFX);
        mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.inventory);
        mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
        mediaPlayer.start();
        menuText = (TextView) findViewById(R.id.textView); // Make sure to change this to new activity counterpart, unless same
        currentSelectIG = 0;
        currentMenuContextIG = 0;
        contextSelectIG = 0;
        nodeSize = 0;
       //  TEST: Use this code for only as a last resort

        menuText.setText((String) omegaMenu[0]);
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onPause(){
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
        super.onResume();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

        public void addItem(ItemNode attach){
        if(nodeSize == 0){
            attach.setNext(attach);
            attach.setPrevious(attach);
            fatherNode = attach;
            childNode = fatherNode;
            currentNode = fatherNode;
            nodeSize++;
        }
        else{
            attach.setNext(fatherNode);
            attach.setPrevious(childNode);
            childNode.setNext(attach);
            childNode = attach;
            fatherNode.setPrevious(childNode);
            currentNode = fatherNode;
            nodeSize++;
        }
    }

    public boolean removeItem(ItemNode compare){
        ItemNode temp = fatherNode;
        while(true) {
            temp = temp.getNext();
            if (compare.compareTo(temp) == 0) {
                if(fatherNode.compareTo(temp) == 0){
                    fatherNode = fatherNode.getNext();
                    currentNode = fatherNode;
                }
                if(childNode.compareTo(temp) == 0){
                    childNode = childNode.getPrevious();
                }
                temp.getPrevious().setNext(temp.getNext());
                temp.getNext().setPrevious(temp.getPrevious());
                nodeSize--;
                temp = null;
                Runtime r = Runtime.getRuntime();
                r.gc();
                return true;
            }
            else if(temp.compareTo(fatherNode) == 0){
                return false;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
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
                for(int x = 0; x < currentMenuContextIG; x++){
                    loc = (loc + menuSize[x]);
                }
                if ((x1 < x2) && (Math.abs(x1 - x2) > 400)) {
                    slide.start();
                    if(currentMenuContextIG == 2 && nodeSize != 0){
                        currentNode = currentNode.getPrevious();
                        hammer = Uri.parse((primer + currentNode.getData().getName()));
                        mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                        menuText.setText((String) currentNode.getData().getName());
                    }
                    else if(currentMenuContextIG == 2 && nodeSize == 0){
                        currentMenuContextIG = 0;
                        mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.noitem);
                        menuText.setText((String) omegaMenu[0]);
                        currentSelectIG = 0;
                        contextSelectIG = 0;
                    }
                    else {
                        contextSelectIG = Math.abs((currentSelectIG - 1) % menuSize[currentMenuContextIG]);
                        mediaPlayer = MediaPlayer.create(InGameMenu.this, omegaMenuVoice[contextSelectIG + loc]);
                        menuText.setText((String) omegaMenu[loc + contextSelectIG]);
                        currentSelectIG--;
                    }
                    mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                  mediaPlayer.start();
                }
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                    slide.start();
                    if(currentMenuContextIG == 2 && nodeSize != 0){
                        currentNode = currentNode.getNext();
                        hammer = Uri.parse((primer + currentNode.getData().getName()));
                        mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                        menuText.setText((String) currentNode.getData().getName());
                    }
                    else if(currentMenuContextIG == 2 && nodeSize == 0){
                        currentMenuContextIG = 0;
                        mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.noitem);
                        menuText.setText((String) omegaMenu[0]);
                        currentSelectIG = 0;
                        contextSelectIG = 0;
                    }
                    else {
                        contextSelectIG = Math.abs((currentSelectIG + 1) % menuSize[currentMenuContextIG]);
                        mediaPlayer = MediaPlayer.create(InGameMenu.this, omegaMenuVoice[contextSelectIG + loc]);
                        menuText.setText((String) omegaMenu[loc + contextSelectIG]);
                        currentSelectIG++;
                    }
                    mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                   mediaPlayer.start();
                } else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    switch (currentMenuContextIG) {
                        /**
                         * Case 0 is the menu, we leave to go back to the game
                         */
                        case 0:
                            currentNode = fatherNode;
                            finish();
                            break;
                        /**
                         * Case 1 is the options menu, case 2 is the inventory, takes you to the menu.
                         */
                        case 1:
                        case 2:
                            context.start();
                            currentMenuContextIG = 0;
                            mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.inventory);
                            mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                            mediaPlayer.start();
                            menuText.setText((String) omegaMenu[0]);
                            currentSelectIG = 0;
                            contextSelectIG = 0;
                    }
                } else if ((y1 < y2) && (Math.abs(y1 - y2) > 400)) {
                    mediaPlayer.start();
                }
                else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))) {
                    if (currentMenuContextIG == 3){
                       // goto previous and send back data
                        finish();
                    }
                    else{
                        context.start();
                        switch (loc + contextSelectIG) {
                            case 0:

                                if(nodeSize != 0) {
                                    currentMenuContextIG = 2;
                                    hammer = Uri.parse((primer + currentNode.getData().getName()));
                                    mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                                    mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                                    mediaPlayer.start();
                                    menuText.setText((String) currentNode.getData().getName());
                                }
                                else{
                                    mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.noitem);
                                }
                                break;
                            case 1:
                                currentMenuContextIG = 1;
                                mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.soundfx);
                                mediaPlayer.setVolume(volumeControl.voiceFX, volumeControl.voiceFX);
                                mediaPlayer.start();
                                menuText.setText(omegaMenu[2]);
                                break;
                            case 2:
                                if(volumeControl.soundFX == 0.3f){
                                    volumeControl.soundFX = 0.6f;
                                } else if(volumeControl.soundFX == 0.6f){
                                    volumeControl.soundFX = 1.0f;
                                } else if(volumeControl.soundFX == 1.0f){
                                    volumeControl.soundFX = 0.3f;
                                }
                                slide.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                context.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                break;
                            case 3:
                                if(volumeControl.voiceFX == 0.3f){
                                    volumeControl.voiceFX = 0.6f;
                                } else if(volumeControl.voiceFX == 0.6f){
                                    volumeControl.voiceFX = 1.0f;
                                } else if(volumeControl.voiceFX == 1.0f){
                                    volumeControl.voiceFX = 0.3f;
                                }
                                break;
                            case 4:
                                if(volumeControl.ambianceFX == 0.3f){
                                    volumeControl.ambianceFX = 0.6f;
                                } else if(volumeControl.ambianceFX == 0.6f){
                                    volumeControl.ambianceFX = 1.0f;
                                } else if(volumeControl.ambianceFX == 1.0f){
                                    volumeControl.ambianceFX = 0.3f;
                                }
                                break;
                            case 5:
                                if(volumeControl.vibrationIntensity == 0.3f){
                                    volumeControl.vibrationIntensity = 0.6f;
                                } else if(volumeControl.vibrationIntensity == 0.6f){
                                    volumeControl.vibrationIntensity = 1.0f;
                                } else if(volumeControl.vibrationIntensity == 1.0f){
                                    volumeControl.vibrationIntensity = 0.3f;
                                }
                                break;
                            case 6:
                                volumeControl.soundFX = 0.6f;
                                volumeControl.voiceFX = 1.0f;
                                volumeControl.ambianceFX = 0.3f;
                                volumeControl.vibrationIntensity = 0.6f;
                                slide.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                context.setVolume(volumeControl.soundFX, volumeControl.soundFX);
                                break;
                            case 8:
                                mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.instructions);
                                mediaPlayer.start();
                                break;
                            case 7:
                                // invokes save state in game, invokes finish state in game to go back to main menu
                                Intent output = new Intent();
                                output.putExtra("ShutOffState", true);
                                setResult(RESULT_OK, output);
                                finish();
                                break;
                        }
                    }
                }
        }
        return false;
    }
}
