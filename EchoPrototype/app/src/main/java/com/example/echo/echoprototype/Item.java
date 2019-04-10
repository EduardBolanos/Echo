package com.example.echo.echoprototype;

public class Item {
    int vibration;
    String name;
    int type;
    String audio;
        public Item(int vibrationData, String itemName, String audioFileName, int itemType){
            vibration = vibrationData;
            name = itemName;
            type = itemType;
            audio = audioFileName;
        }
        public int getVibration(){
            return vibration;
        }
        public String getName(){
            return name;
        }
        public String getAudio(){
            return audio;
        }
        public int getType(){
            return type;
        }
}
