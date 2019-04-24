package com.example.echo.echo_v_1_0_2;

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
    private int currentSelectIG; //Counter
    private int currentMenuContextIG; //Selected Menu
    private int contextSelectIG; //Location in arrays, ex
    //Omega String
    private String menu[] = {"Inventory", "Settings and Navigation", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "Reset Volume Values", "Reset Level",  "Return to Menu", "Instructions"};
    // create these voice files
    private String menuVoice[] = {"inventory", "explain", "soundfx", "voicefx", "R.raw.amfx",
            "vibration", "reset", "resetlevel" , "returntomenu" ,"help"};
    private int menuSize[] = {2, 8};
    public String primer = ("android.resource://" + "com.example.echo.echo_v_1_0_2" + "/raw/"); // Incase things happen such as package reference for public
    private Uri hammer;
    private int loc;
    private boolean resetLevel;
    private MediaPlayer ambiance; // WE ARE APPARENTLY BRITISH DEVELOPERS TODO, get something
    private int nodeSize;
    private ItemNode fatherNode;
    private ItemNode childNode;
    private ItemNode currentNode;
    float x1, x2, y1, y2;
    TextView menuText;
    SoundSettings volumeControl; // Dunno
    /**
     * TEST: Use this code for only as a last resort
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ingamemenu); // CHANGE MENU ACTIVITY TO NEW ONE , BLACK BACKGROUND, WHITE TEXT /*IMPORTANT*/

        //gives memes /*IMPORTANT*/
        nodeSize = 0;
        volumeControl = new SoundSettings();
        resetLevel = false;
        this.loadAndSetItems();
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
        menuText = (TextView) findViewById(R.id.textView); // Make sure to change this to new activity counterpart, unless same
        currentSelectIG = 0;
        currentMenuContextIG = 0;
        contextSelectIG = 0;
        menuText.setText((String) menu[0]);
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onPause(){
        this.saveSettings();
        resetLevel = false;
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
        while(temp != null) {
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
        return false;
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
                    playSoundScape(R.raw.slide, volumeControl.soundFX, volumeControl.soundFX);
                    if(currentMenuContextIG == 2 && nodeSize != 0){
                        currentNode = currentNode.getPrevious();
                        playByUriSoundScape(currentNode.getData().getAuditoryId(),volumeControl.voiceFX,volumeControl.voiceFX);
                        //TODO key sound logic
                        playByUriSoundScape("keys0" + currentNode.getData().getPassCode(),volumeControl.soundFX,volumeControl.soundFX);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        menuText.setText((String) currentNode.getData().getName());
                    }
                    else if(currentMenuContextIG == 2 && nodeSize == 0){
                        currentMenuContextIG = 0;
                        playSoundScape(R.raw.noitem, volumeControl.voiceFX, volumeControl.voiceFX);
                        menuText.setText((String) menu[0]);
                        currentSelectIG = 0;
                        contextSelectIG = 0;
                    }
                    else {
                        contextSelectIG = Math.abs((currentSelectIG - 1) % menuSize[currentMenuContextIG]);
                        playByUriSoundScape(menuVoice[contextSelectIG + loc], volumeControl.voiceFX, volumeControl.voiceFX);
                        menuText.setText((String) menu[loc + contextSelectIG]);
                        currentSelectIG--;
                    }
                }
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                    playSoundScape(R.raw.slide, volumeControl.soundFX, volumeControl.soundFX);
                    if(currentMenuContextIG == 2 && nodeSize != 0){
                        currentNode = currentNode.getNext();
                        playByUriSoundScape(currentNode.getData().getAuditoryId(),volumeControl.voiceFX,volumeControl.voiceFX);
                        //TODO key sound logic
                        playByUriSoundScape("keys0" + currentNode.getData().getPassCode(),volumeControl.soundFX,volumeControl.soundFX);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        menuText.setText((String) currentNode.getData().getName());
                    }
                    else if(currentMenuContextIG == 2 && nodeSize == 0){
                        currentMenuContextIG = 0;
                        playSoundScape(R.raw.noitem, volumeControl.voiceFX, volumeControl.voiceFX);
                        menuText.setText((String) menu[0]);
                        currentSelectIG = 0;
                        contextSelectIG = 0;
                    }
                    else {
                        contextSelectIG = Math.abs((currentSelectIG + 1) % menuSize[currentMenuContextIG]);
                        playByUriSoundScape(menuVoice[contextSelectIG + loc], volumeControl.voiceFX, volumeControl.voiceFX);
                        menuText.setText((String) menu[loc + contextSelectIG]);
                        currentSelectIG++;
                    }
                } else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {
                    switch (currentMenuContextIG) {
                        /**
                         * Case 0 is the menu, we leave to go back to the game
                         */
                        case 0:
                            currentNode = fatherNode;
                            Intent output = new Intent();
                            output.putExtra("ShutOffState", false);
                            output.putExtra("resetLevel", resetLevel);
                            setResult(RESULT_OK, output);
                            this.saveSettings();
                            resetLevel = false;
                            finish();
                            break;
                        /**
                         * Case 1 is the options menu, case 2 is the inventory, takes you to the menu.
                         */
                        case 1:
                        case 2:
                            playSoundScape(R.raw.change,volumeControl.soundFX, volumeControl.soundFX);
                            currentMenuContextIG = 0;
                            playSoundScape(R.raw.inventory,volumeControl.voiceFX, volumeControl.voiceFX);
                            menuText.setText((String) menu[0]);
                            currentSelectIG = 0;
                            contextSelectIG = 0;
                    }
                } else if ((y1 < y2) && (Math.abs(y1 - y2) > 400)) {
                    playByUriSoundScape(menuVoice[contextSelectIG + loc], volumeControl.voiceFX, volumeControl.voiceFX);
                }
                else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))) {
                    if (currentMenuContextIG == 3){
                       // goto previous and send back data if needed for the item
                    }
                    else{
                        playSoundScape(R.raw.change, volumeControl.soundFX, volumeControl.soundFX);
                        switch (loc + contextSelectIG) {
                            case 0:

                                if(nodeSize != 0) {
                                    currentMenuContextIG = 2;
                                    playByUriSoundScape(currentNode.getData().getAuditoryId(),volumeControl.voiceFX,volumeControl.voiceFX);
                                    //TODO key sound logic
                                    playByUriSoundScape("keys0" + currentNode.getData().getPassCode(),volumeControl.soundFX,volumeControl.soundFX);
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException ex) {
                                        Thread.currentThread().interrupt();
                                    }
                                    menuText.setText((String) currentNode.getData().getName());
                                }
                                else{
                                    playSoundScape(R.raw.noitem, volumeControl.voiceFX, volumeControl.voiceFX);
                                }
                                break;
                            case 1:
                                currentMenuContextIG = 1;
                                contextSelectIG = 0;
                                currentSelectIG = 0;
                                playSoundScape(R.raw.soundfx, volumeControl.soundFX, volumeControl.soundFX);
                                menuText.setText(menu[2]);
                                break;
                            case 2:
                                if(volumeControl.soundFX == 0.3f){
                                    volumeControl.soundFX = 0.6f;
                                } else if(volumeControl.soundFX == 0.6f){
                                    volumeControl.soundFX = 1.0f;
                                } else if(volumeControl.soundFX == 1.0f){
                                    volumeControl.soundFX = 0.3f;
                                }
                                playSoundScape(R.raw.soundfx, volumeControl.soundFX, volumeControl.soundFX);
                                break;
                            case 3:
                                if(volumeControl.voiceFX == 0.3f){
                                    volumeControl.voiceFX = 0.6f;
                                } else if(volumeControl.voiceFX == 0.6f){
                                    volumeControl.voiceFX = 1.0f;
                                } else if(volumeControl.voiceFX == 1.0f){
                                    volumeControl.voiceFX = 0.3f;
                                }
                                playSoundScape(R.raw.voicefx, volumeControl.voiceFX, volumeControl.voiceFX);
                                break;
                            case 4:
                                if(volumeControl.ambianceFX == 0.3f){
                                    volumeControl.ambianceFX = 0.6f;
                                } else if(volumeControl.ambianceFX == 0.6f){
                                    volumeControl.ambianceFX = 1.0f;
                                } else if(volumeControl.ambianceFX == 1.0f){
                                    volumeControl.ambianceFX = 0.3f;
                                }
                                playSoundScape(R.raw.amfx, volumeControl.ambianceFX, volumeControl.ambianceFX);
                                break;
                            case 5:
                                if (volumeControl.vibrationIntensity == 0.3f) {
                                    volumeControl.vibrationIntensity = 0.6f;
                                } else if (volumeControl.vibrationIntensity == 0.6f) {
                                    volumeControl.vibrationIntensity = 1.0f;
                                } else if (volumeControl.vibrationIntensity == 1.0f) {
                                    volumeControl.vibrationIntensity = 0.3f;
                                }
                                playSoundScape(R.raw.slide, volumeControl.vibrationIntensity, volumeControl.vibrationIntensity);
                                break;
                            case 6:
                                volumeControl.soundFX = 0.6f;
                                volumeControl.voiceFX = 1.0f;
                                volumeControl.ambianceFX = 0.3f;
                                volumeControl.vibrationIntensity = 0.6f;
                                playSoundScape(R.raw.reset, volumeControl.voiceFX, volumeControl.voiceFX);
                                break;
                            case 7:
                                resetLevel = true;
                                playSoundScape(R.raw.resetlevel, volumeControl.voiceFX, volumeControl.voiceFX);
                                break;
                            case 8:
                                // invokes save state in game, invokes finish state in game to go back to main menu
                                Intent output = new Intent();
                                output.putExtra("ShutOffState", true);
                                output.putExtra("resetLevel", resetLevel);
                                setResult(RESULT_OK, output);
                                this.saveSettings();
                                resetLevel = false;
                                finish();
                                break;
                            case 9:
                                playSoundScape(R.raw.instructions, volumeControl.voiceFX, volumeControl.voiceFX);
                                break;
                        }
                    }
                }
        }
        return false;
    }

    public void saveSettings(){
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
    public void loadAndSetItems() {
        FileInputStream items;
        String concatinator = "";
        int data = 0;
        int state = 0;
        Item newItem = null;
        ItemNode newNode = null;
        String passCode = "";
        String name = "";
        String audio = "";
        int status = 0;

        try {
            items = openFileInput("items");
            try {
                data = items.read();
            } catch (java.io.IOException e) {
            }
            if(data == 'R'){
                nodeSize = 0;
                childNode = null;
                currentNode = null;
                fatherNode = null;
                Runtime r = Runtime.getRuntime();
                r.gc();
            }
            try {
                data = items.read();
            } catch (java.io.IOException e) {
            }
            while (data != '@') {

                if (((char) data != '\n' && ((char) data > 31))) {
                    if (data != 35) {
                        concatinator = concatinator + ((char) data);
                    } else {
                        switch (state) {
                            case 0:
                                passCode = concatinator;
                                concatinator = "";
                                state = 1;
                                break;
                            case 1:
                                name = concatinator;
                                concatinator = "";
                                state = 2;
                                break;
                            case 2:
                                audio = concatinator;
                                concatinator = "";
                                state = 3;
                                break;
                            case 3:
                                concatinator = ""; // Stub
                                state = 4;
                                break;
                            case 4:
                                status = Integer.parseInt(concatinator);
                                concatinator = "";
                                state = 5;
                                break;
                                case 5:
                                newItem = new Item(passCode, null, Integer.parseInt(concatinator));
                                newItem.setName(name);
                                newItem.setAuditoryId(audio);
                                newNode = new ItemNode(newItem, null, null);
                                if(status == 1){
                                    addItem(newNode);
                                } else{
                                    removeItem(newNode);
                                }
                                concatinator = "";
                                state = 0;
                        }
                    }
                }
                try {
                    data = items.read();
                } catch (java.io.IOException e) {
                }
            }
        }
           catch (Exception e) {
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
