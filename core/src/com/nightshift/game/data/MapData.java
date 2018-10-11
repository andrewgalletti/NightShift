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
                fileName = "Maps/menu.tmx";
                break;
            case 1:
                fileName = "Maps/supereasymap.tmx";
                break;
            case 2:
                fileName = "Maps/firstmapnoghost.tmx";
                break;
            case 3:
                fileName = "Maps/noghostmaze.tmx";
                break;
            case 4:
                fileName = "Maps/firstghostrun.tmx";
                break;
            case 5:
                fileName = "Maps/mechanicmap.tmx";
                break;
            case 6:
                fileName = "Maps/openbeginner.tmx";
                break;
            case 7:
                fileName = "Maps/ghostrooms.tmx";
                break;
            case 8:
                fileName = "Maps/easymap2.tmx";
                break;
            case 9:
                fileName = "Maps/concentricmap2.tmx";
                break;
            case 10:
                fileName = "Maps/spiralmap2.tmx";
                break;
            case 11:
                fileName = "Maps/learningmap.tmx";
                break;
            case 12:
                fileName = "Maps/zigmap2.tmx";
                break;
            case 13:
                fileName = "Maps/anothermap.tmx";
                break;
            case 14:
                fileName = "Maps/test.tmx";
                break;
            case 15:
                fileName = "Maps/bigmappy.tmx";
                break;
            case 16:
                fileName = "Maps/mazemap.tmx";
                break;
        }
        return fileName;
    }

    public float spriteSize(int levelIndex){
        /**
         * Determines sprite scale factor for janitor and ghost sprites.
         */

        switch(levelIndex){
            case 14:
                return 0.5f;
            case 15:
                return 0.5f;
            case 16:
                return 0.5f;
        }
        return 1;
    }
}
