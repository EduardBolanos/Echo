package com.example.echo.echo_v_1_0_2;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.ArrayList;


public class Enemy
{
    public int[] position = new int[2];
    public int orientation;//0 North, 1 East, 2 South, 3 West
    MediaPlayer EnemyRoar;
    MediaPlayer EnemyStep;

    Context context;

    public Enemy(Context context, int locx, int locy, int orientation) {
        context = context;
        position[0] = locx;
        position[1] = locy;
        orientation = orientation;
        enemyRoar = MediaPlayer.create(context, R.raw.EnemyRoar);
        enemyStep = MediaPlayer.create(context, R.raw.EnemyStep);
    }

    public void turnLeft() {
        orientation++;
        if(orientation == 4)
        {
            orientation = 0;
        }
    }

    public void turnRight() {
        orientation--;
        if (orienation == (-1))
        {
            orientation = 3;
        }
    }

    public void enemyTurn(LevelManager levelManager, Player player)
    {
        int[] playerPosition = player.getPosition();
        for(int i = 0; i < levelManager.mEnemies.size(); i++)
        {
            //handle death if player walked into an enemy
            if (levelManager.get(i).position[0] == playerPosition[0] && levelManager.get(i).position[1] == playerPosition[1])
            {
                PlayerDeath();
            }
            sentryMovement();
            //handle death if enemy walked into a player
            if (levelManager.get(i).position[0] == player.getPosition[0] && levelManager.get(i).position[1] == player.getPosition[1])
            {
                PlayerDeath();
            }
        }
    }
    
    public void step(LevelManager levelManager, Player player){
        switch (orientation) {
            case 0:
                position[1] = position[1] + 1;
                break;
            case 1:
                position[0] = position[0] + 1;
                break;
            case 2:
                position[1] = position[1] - 1;
                break;
            case 3:
                position[0] = position[0] - 1;
                break;
        }
        if(levelManager.getTileAtCoord(position).getType() == 'w') {
            switch (orientation) {
                case 0:
                    position[1] = position[1] - 1;
                    break;
                case 1:
                    position[0] = position[0] - 1;
                    break;
                case 2:
                    position[1] = position[1] + 1;
                    break;
                case 3:
                    position[0] = position[0] + 1;
                    break;
            }
            turnLeft();

            step(levelManager, player);

            //play EnemyStep based on direction of player
            enemyStep.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean PlayerSighted(LevelManager levelManager, Player player)
    {
        //check forward until wall
        int[2] newPosition;
        int[] playerPosition = player.getPosition();
        newPosition[0] = enemy.position[0];
        newPosition[1] = enemy.position[1];
        while(levelManager.getTileAtCoord(newPosition) != 'w') {
            switch (orientation) {
                case 0:
                    newPosition[1] = newPosition[1] - 1;
                    break;
                case 1:
                    newPosition[0] = newPosition[0] - 1;
                    break;
                case 2:
                    newPosition[1] = NewPosition[1] + 1;
                    break;
                case 3:
                    newPosition[0] = newPosition[0] + 1;
                    break;
            }
            //if newPosition = player position reutrn true
            if (newPosition[0] == playerPosition && newPosition[1] = playerPosition) {
                return true;
                //play EnemyRoar
                enemyRoar.start();
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

    public void SentryMovement(LevelManager levelManager, Player player){
        //if open left look left
        boolean openLeft = false;
        int [2] newPosition;
        newPosition[0] = position[0];
        newPosition[1] = position[1];
        switch (orientation) {
            case 0:
                newPosition[0] = newPosition[0] - 1;
                if(levelManager.getTileAtCoord(newPosition).getType() == 'f')
                {
                    openLeft = true;
                }
                break;
            case 1:
                newPosition[1] = newPosition[1] + 1;
                if(levelManager.getTileAtCoord(newPosition).getType() == 'f')
                {
                    openLeft = true;
                }
                break;
            case 2:
                newPosition[1] = newPosition[1] - 1;
                if(levelManager.getTileAtCoord(newPosition).getType() == 'f')
                {
                    openLeft = true;
                }
                break;
            case 3:
                newPosition[0] = newPosition[0] - 1;
                if(levelManager.getTileAtCoord(newPosition).getType() == 'f')
                {
                    openLeft = true;
                }
                break;
        }
        //if enemy spotted turn step once
        if(openLeft)
        {
            turnLeft();
            if(PlayerSighted(levelManager, player)) {
                step(levelManager, player);
            }
            else
            {
                turnRight();
            }
        }
        //lookforwad if enemy spotted step once
        if(PlayerSighted(levelManager, player))
        {
            step(levelManager, player);
        }
        //step once
        step(levelManager, player);
    }
}
