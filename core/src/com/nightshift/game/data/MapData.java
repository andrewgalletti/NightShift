package com.nightshift.game.data;

import com.badlogic.gdx.math.Vector2;
import com.nightshift.game.data.Constants;

import java.util.ArrayList;

public class MapData {

    public Vector2 previousScreenDimensions;

    public MapData() {
        previousScreenDimensions = new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
    }

    public String getFileName(int levelIndex) {
        String fileName = "";
        switch(levelIndex) {
            case 0:
                fileName = "Maps/startmap.tmx";
                break;
            case 1:
                fileName = "Maps/easymap.tmx";
                break;
            case 2:
                fileName = "Maps/hardmap.tmx";
                break;
            case 3:
                fileName = "Maps/spiralmap.tmx";
                break;
        }
        return fileName;
    }

    public ArrayList<Vector2> getEnemies(int levelIndex) {
        ArrayList<Vector2> enemyLocations = new ArrayList<Vector2>();
        switch(levelIndex) {
            case 0:
                enemyLocations.add(new Vector2(70, 250));
                break;
            case 1:
                enemyLocations.add(new Vector2(125, 375));
                enemyLocations.add(new Vector2(375, 375));
                break;
            case 2:
                enemyLocations.add(new Vector2(250,125));
                enemyLocations.add(new Vector2(375, 125));
                enemyLocations.add(new Vector2(62,427));
                //enemyLocations.add(new Vector2(400,390));
                break;
            case 3:
                enemyLocations.add(new Vector2(100, 360));
                enemyLocations.add(new Vector2(400, 125));
                enemyLocations.add(new Vector2(200, 125));
                enemyLocations.add(new Vector2(375, 400));
                break;
        }
        return enemyLocations;
    }

    public Vector2 janitorSpawn(int levelIndex) {
        Vector2 spawn = new Vector2();
        switch(levelIndex) {
            case 0:
                spawn.x = 160;
                spawn.y = 250;
                break;
            case 1:
                spawn.x = 45;
                spawn.y = 45;
                break;
            case 2:
                spawn.x = 45;
                spawn.y = 45;
                break;
            case 3:
                spawn.x = 45;
                spawn.y = 45;
                break;
        }

        return spawn;
    }
}
