package com.example.echo.echo_v_1_0_2;

import java.io.FileInputStream;

public class SoundSettings {
    protected float soundFX;
    protected float voiceFX;
    protected float ambianceFX;
    protected float vibrationIntensity;
    public SoundSettings(){

    }
    public void loadSettings(FileInputStream settings){
        int data = 0;
        String settingValue = "";
        int state = 0;
        while (true) {
            try {
                data = settings.read();
            } catch (java.io.IOException e) {
            }
            if(data > 31){
                if(data != '#'){
                    settingValue = settingValue + ((char)data);
                }
                else if(state == 0){
                    soundFX = Float.parseFloat(settingValue);
                    settingValue = "";
                    state++;
                }
                else if(state == 1){
                    voiceFX = Float.parseFloat(settingValue);
                    settingValue = "";
                    state++;
                }
                else if(state == 2){
                    ambianceFX = Float.parseFloat(settingValue);
                    settingValue = "";
                    state++;
                }
                else if(state == 3){
                    vibrationIntensity = Float.parseFloat(settingValue);
                    settingValue = "";
                    state++;
                }
            }
            if(state == 4){
                break;
            }
        }

    }
}
