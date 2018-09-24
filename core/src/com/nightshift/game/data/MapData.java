package com.nightshift.game.data;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class MapData {
    /**
     * Stores information about level maps, ghost spawn locations and janitor spawn locations.
     */
    public Vector2 previousScreenDimensions;

    public MapData() {
        previousScreenDimensions = new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
    }

    public String getFileName(int levelIndex) {
        /**
         * Each case is a filepath for a map stored in assets. To add levels, simply add a case here.
         */
        String fileName = "";
        switch(levelIndex) {
            case 0:
                fileName = "Maps/startmap2.tmx";
                break;
            case 1:
                fileName = "Maps/easymap2.tmx";
                break;
            case 2:
                fileName = "Maps/concentricmap2.tmx";
                break;
            case 3:
                fileName = "Maps/zigmap2.tmx";
                break;
            case 4:
                fileName = "Maps/spiralmap2.tmx";
                break;
            case 5:
                fileName = "Maps/learningmap.tmx";
                break;
            case 6:
                fileName = "Maps/test.tmx";
                break;
        }
        return fileName;
    }

    public ArrayList<Vector2> getEnemySpawnLocations(int levelIndex) {
        /**
         * Each case is for one level. One add method add one ghost.
         */
        ArrayList<Vector2> enemyLocations = new ArrayList<Vector2>();
        switch(levelIndex) {
            case 0:
                enemyLocations.add(new Vector2(10, 250));
                break;
            case 1:
                enemyLocations.add(new Vector2(375, 350));
                enemyLocations.add(new Vector2(125, 350));
                break;
            case 2:
                enemyLocations.add(new Vector2(450, 300));
                enemyLocations.add(new Vector2(50, 300));
                break;
            case 3:
                enemyLocations.add(new Vector2(50, 250));
                enemyLocations.add(new Vector2(400, 400));
                break;
            case 4:
                enemyLocations.add(new Vector2(250, 125));
                enemyLocations.add(new Vector2(375, 125));
                enemyLocations.add(new Vector2(150, 427));
                break;
            case 5:
                enemyLocations.add(new Vector2(250,45));
                enemyLocations.add(new Vector2(45,350));
                enemyLocations.add(new Vector2(350,200));
                break;
            case 6:
                enemyLocations.add(new Vector2(400,45));
                enemyLocations.add(new Vector2(200,45));
                enemyLocations.add(new Vector2(400,100));
                enemyLocations.add(new Vector2(200,100));
                enemyLocations.add(new Vector2(300,300));
                enemyLocations.add(new Vector2(100,400));
                break;
        }
        return enemyLocations;
    }

    public Vector2 janitorSpawn(int levelIndex) {
        /**
         * Each case is for one level.
         */
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
                spawn.x = 250;
                spawn.y = 100;
                break;
            case 3:
                spawn.x = 45;
                spawn.y = 45;
                break;
            case 4:
                spawn.x = 45;
                spawn.y = 45;
                break;
            case 5:
                spawn.x = 45;
                spawn.y = 45;
                break;
            case 6:
                spawn.x = 50;
                spawn.y = 50;
                break;
        }

        return spawn;
    }

    public String spriteSize(int levelIndex){
        /**
         * Determines sprite size of janitor and ghosts for each map index.
         * returns smallModifier if small sprite size is needed,
         * returns largeModifier if large sprite is needed.
         */
        String smallModifier = " Small";
        String largeModifier = "";
        switch(levelIndex){
            case 0:
                return largeModifier;
            case 1:
                return largeModifier;
            case 2:
                return largeModifier;
            case 3:
                return largeModifier;
            case 4:
                return largeModifier;
            case 5:
                return largeModifier;
            case 6:
                return smallModifier;

        }
        return largeModifier;
    }
}
