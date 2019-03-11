package com.example.eduardbolanos642.echoprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView menuText;
    int currentSelect;
    int currentMenuContext;
    String mainMenuOne[] = {"Start","Settings"};
    String mainMenuTwo[] = {"1","2","3","4"};
    String mainMenuThree[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //gives memes
        menuText = (TextView) findViewById(R.id.textView);
        currentSelect = 0;
        currentMenuContext = 0;
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
                //on click
                if(x1 == x2){
//                    Intent i = new Intent(MainActivity.this, NewGame.class);
//                    startActivity(i);
                    if((currentMenuContext == 0) && (Math.abs((currentSelect) % 2) == 1)){
                        currentSelect = 0;
                        currentMenuContext = 1;
                        menuText.setText((String) mainMenuTwo[currentSelect]);
                    }
                }
                //swipe left
                else if( (x1 < x2) && (Math.abs(x1-x2) > 400) ) {
                    //about to blow Timothy's world
                    if (currentMenuContext == 0) {
                        menuText.setText((String) mainMenuOne[Math.abs((currentSelect-1) % 2)]);
                        currentSelect--;
                    }
                    else if (currentMenuContext == 1) {
                        menuText.setText((String) mainMenuTwo[Math.abs((currentSelect-1) % 4)]);
                        currentSelect--;
                    }
                }
                //swipe right
                else if( (x1 > x2) && (Math.abs(x1-x2) > 400) ){
                    if (currentMenuContext == 0) {
                        menuText.setText((String) mainMenuOne[Math.abs((currentSelect+1)) % 2]);
                        currentSelect++;
                        }
                        else if (currentMenuContext == 1) {
                        menuText.setText((String) mainMenuTwo[Math.abs((currentSelect+1)) % 4]);
                        currentSelect++;
                    }
                    }

                break;

        }
        return false;
    }

}
