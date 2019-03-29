package com.example.eduardbolanos642.echoprototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class InGameMenu extends AppCompatActivity {
    int currentSelect; //Counter
    int currentMenuContext; //Selected Menu
    int contextSelect; //Location in arrays, ex
    //Omega String
    String omegaMenu[] = {"Inventory", "Back To Menu", "Settings"};
    String omegaMenuVoice[] = {};
    int menuSize = 3;
    int nodeSize = 0;
    ItemNode fatherNode;
    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gameplay);
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
                if ((x1 < x2) && (Math.abs(x1 - x2) > 400)) {
                } else if ((x1 > x2) && (Math.abs(x1 - x2) > 400)) {

                } else if ((y1 > y2) && (Math.abs(y1 - y2) > 400)) {

                } else if (((Math.abs(x1 - x2) < 50) && (Math.abs(y1 - y2) < 50))
                        || ((y1 > y2) && (Math.abs(y1 - y2) > 400))) {

                }
        }
        return false;
    }
}
