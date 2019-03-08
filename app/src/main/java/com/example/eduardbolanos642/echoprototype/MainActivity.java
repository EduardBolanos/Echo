package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnGoSecond (View view){
        startActivity(new Intent(this, NewGameActivity.class));
    }

    public boolean onTouchEvent(MotionEvent touchMeDaddy){
        switch(touchMeDaddy.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x1 = touchMeDaddy.getX();
                    y1 = touchMeDaddy.getY();
                    break;

                case MotionEvent.ACTION_UP:
                    x2 = touchMeDaddy.getX();
                    y2 = touchMeDaddy.getY();
                    if(x1 < x2){
                        Intent intent = new Intent(MainActivity.this, SwipeLeft.class);
                        startActivity(intent);
                    }else if(x1 > x2){
                        Intent intent = new Intent(MainActivity.this, SwipeRight.class);
                        startActivity(intent);
                    }
                    break;
        }
        return false;
    }
}
