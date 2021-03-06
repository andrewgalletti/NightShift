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
        String fileName = "Maps/Official Maps/";
        switch(levelIndex) {
            case 0:
                fileName += "menu.tmx";
                break;
            case 1:
                fileName += "supereasymap.tmx";
                break;
            case 2:
                fileName += "firstmapnoghost.tmx";
                break;
            case 3:
                fileName += "mechanicmap.tmx";
                break;
            case 4:
                fileName += "morethanoneghost.tmx";
                break;
            case 5:
                fileName += "easymap2.tmx";
                break;
            case 6:
                fileName += "learningmap.tmx";
                break;
            case 7:
                fileName += "anothermap.tmx";
                break;
            case 8:
                fileName += "ghostrooms.tmx";
                break;
            case 9:
                fileName += "blocklevel.tmx";
                break;
            case 10:
                fileName += "spiralmap2.tmx";
                break;
            case 11:
                fileName += "differentsizeghosts.tmx";
                break;
            case 12:
                fileName += "noghostmaze.tmx";
                break;
            case 13:
                fileName += "openbeginner.tmx";
                break;
            case 14:
                fileName += "butterfly.tmx";
                break;
            case 15:
                fileName += "zipzap.tmx";
                break;
            case 16:
                fileName += "openbeginner.tmx";
                break;
            case 17:
                fileName += "bigmappy.tmx";
                break;
            case 18:
                fileName += "test.tmx";
                break;
            case 19:
                fileName += "mazemap.tmx";
                break;
            case 20:
                fileName += "wallOfGhosts.tmx";
                break;
            case 21:
                fileName += "TheEnd.tmx";
                break;
        }
        return fileName;
    }

    public float spriteSize(int levelIndex){
        /**
         * Determines sprite scale factor for janitor and ghost sprites.
         */
        if(levelIndex >= 14){
            return 0.5f;
        }
        return 1;
    }
}
