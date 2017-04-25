package com.nightshift.game;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class MapData {

    public MapData() {}

    public String getFileName(int levelIndex) {
        String fileName = "";
        switch(levelIndex) {
            case 0:
                fileName = "Maps/easymap.tmx";
                break;
            case 1:
                fileName = "Maps/spiralmap.tmx";
                break;
            case 2:
                fileName = "Maps/hardmap.tmx";
                break;
        }
        return fileName;
    }

    public ArrayList<Vector2> getEnemies(int levelIndex) {
        ArrayList<Vector2> enemyLocations = new ArrayList<Vector2>();
        switch(levelIndex) {
            case 0:
                enemyLocations.add(new Vector2(125, 375));
                enemyLocations.add(new Vector2(250, 375));
                enemyLocations.add(new Vector2(375, 375));
                break;
            case 1:
                enemyLocations.add(new Vector2(250,125));
                enemyLocations.add(new Vector2(375, 125));
                enemyLocations.add(new Vector2(62,427));
                enemyLocations.add(new Vector2(400,390));
                break;
            case 2:
                enemyLocations.add(new Vector2(100, 360));
                enemyLocations.add(new Vector2(400, 125));
                enemyLocations.add(new Vector2(200, 125));
                enemyLocations.add(new Vector2(375, 400));
                break;
        }
        return enemyLocations;
    }
}
