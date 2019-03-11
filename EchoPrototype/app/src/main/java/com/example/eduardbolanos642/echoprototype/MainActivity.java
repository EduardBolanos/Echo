package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnGoSecond (View view){
        startActivity(new Intent(this, NewGame.class));
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
                if(x1 > x2){
                    Intent i = new Intent(MainActivity.this, NewGame.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

}
