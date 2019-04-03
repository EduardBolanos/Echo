package com.example.eduardbolanos642.echoprototype;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;

public class InGameMenu extends AppCompatActivity {
    int currentSelectIG; //Counter
    int currentMenuContextIG; //Selected Menu
    int contextSelectIG; //Location in arrays, ex
    //Omega String
    String omegaMenu[] = {"Inventory", "Settings and Navigation", "Sound FX Volume", "Voice Volume", "Ambiance & Music",
            "Vibration Level", "Return to Menu", "Instructions"};
    // create these voice files
    String omegaMenuVoice[] = {"inventory", "explain", "soundfx", "voicefx", "amfx", "vibration", "return", "help"};
    int menuSize[] = {2, 6};
    String primer = ("android.resource://" + "com.example.eduardbolanos642.echoprototype" + "/raw/");
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
    //TEST
    Item one = new Item(1, "Rusty Key", "boi", 1);
    Item two = new Item(1, "Not a Rusty Key", "boi", 1);
    Item three = new Item(1, "Wooden Key", "boi", 1);
    ItemNode oneadd = new ItemNode(one, null, null);
    ItemNode twoadd = new ItemNode(two, null, null);
    ItemNode threeadd = new ItemNode(three, null, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ingamemenu); // CHANGE MENU ACTIVITY TO NEW ONE , BLACK BACKGROUND, WHITE TEXT /*IMPORTANT*/

        //gives memes /*IMPORTANT*/
        menuText = (TextView) findViewById(R.id.textView); // Make sure to change this to new activity counterpart, unless same
        currentSelectIG = 0;
        currentMenuContextIG = 0;
        contextSelectIG = 0;
        nodeSize = 0;
        //TEST
        this.addItem(oneadd);
        this.addItem(twoadd);
        this.addItem(threeadd);
        this.removeItem(twoadd);
        this.addItem(twoadd);
        this.removeItem(oneadd);
        this.addItem(oneadd);
        this.removeItem(oneadd);
        this.addItem(oneadd);
        menuText.setText((String) omegaMenu[0]);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onPause(){
        super.onPause();

    }
    @Override
    protected void onResume(){
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
                    if(currentMenuContextIG == 2 && nodeSize != 0){
                        currentNode = currentNode.getPrevious();
                        //hammer = Uri.parse((primer + currentNode.getData().getAudio()));
                     //   mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                        menuText.setText((String) currentNode.getData().getName());
                    }
                    else if(currentMenuContextIG == 2 && nodeSize == 0){
                        currentMenuContextIG = 0;
                       // mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.start); // Replace with no Item /*IMPORTANT*/
                        menuText.setText((String) omegaMenu[0]);
                        currentSelectIG = 0;
                        contextSelectIG = 0;
                    }
                    else {
                        contextSelectIG = Math.abs((currentSelectIG - 1) % menuSize[currentMenuContextIG]);
                       // hammer = Uri.parse((primer + omegaMenuVoice[contextSelect + loc]));
                        //mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                        menuText.setText((String) omegaMenu[loc + contextSelectIG]);
                        currentSelectIG--;
                    }
                }
                else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)){
                    if(currentMenuContextIG == 2 && nodeSize != 0){
                        currentNode = currentNode.getNext();
                        //hammer = Uri.parse((primer + currentNode.getData().getAudio()));
                        //mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                        menuText.setText((String) currentNode.getData().getName());
                    }
                    else if(currentMenuContextIG == 2 && nodeSize == 0){
                        currentMenuContextIG = 0;
                        //mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.start);
                        menuText.setText((String) omegaMenu[0]);
                        currentSelectIG = 0;
                        contextSelectIG = 0;
                    }
                    else {
                        contextSelectIG = Math.abs((currentSelectIG + 1) % menuSize[currentMenuContextIG]);
                        //hammer = Uri.parse((primer + omegaMenuVoice[contextSelect + loc]));
                        //mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                        menuText.setText((String) omegaMenu[loc + contextSelectIG]);
                        currentSelectIG++;
                    }

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
                            currentMenuContextIG = 0;
                           // mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.start);
                            menuText.setText((String) omegaMenu[0]);
                            currentSelectIG = 0;
                            contextSelectIG = 0;
                    }
                } else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))) {
                    if (currentMenuContextIG == 3){
                       // goto previous and send back data
                        finish();
                    }
                    else{
                        switch (loc + contextSelectIG) {
                            case 0:

                                if(nodeSize != 0) {
                                    currentMenuContextIG = 2;
                                    //hammer = Uri.parse((primer + currentNode.getData().getAudio()));
                                    //      mediaPlayer = MediaPlayer.create(InGameMenu.this, hammer);
                                    menuText.setText((String) currentNode.getData().getName());
                                }
                                else{
                                    // Do something with no items here.
                                }
                                break;
                            case 1:
                                currentMenuContextIG = 1;
                                mediaPlayer = MediaPlayer.create(InGameMenu.this, R.raw.soundfx);
                                menuText.setText(omegaMenu[2]);
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
                                // invokes save state in game, invokes finish state in game to go back to main menu
                                finish();
                                break;
                            case 7:
                                break;
                        }
                    }
                }
        }
        return false;
    }
}
