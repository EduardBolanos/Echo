
package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class SwipeLeft extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_left);
    }

    float x1, x2, y1, y2;
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if(x1 == x2){
                    Intent i = new Intent(SwipeLeft.this, NewGame.class);
                    startActivity(i);
                }
                else if((x1 < x2 || x1 > x2) && (Math.abs(x1-x2) > 400)){
                    //about to blow Timothy's world
                    Intent left = new Intent(SwipeLeft.this, MainActivity.class);
                    startActivity(left);
                }
                break;

        }
        return false;
    }
}
