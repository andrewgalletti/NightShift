package com.nightshift.game.data;

public class Constants {

    //sets the total amount of levels in game
    public static final int MAX_NUM_LEVELS = 9;
    public static final int END_LEVEL_INDEX = MAX_NUM_LEVELS - 1;

    //Used to convert from Box2d units to relative coordinate system.
    public static final float PIXELS_TO_METERS = 100f;
    public static final int VIEWPORT_HEIGHT = 500;
    public static final int VIEWPORT_WIDTH = 500;

    //Sound Volumes expressed as percentages of original volume.
    public static final float INTO_PURSUIT_VOLUME = .3f;
    public static final float TAKE_DAMAGE_VOLUME = .5f;
    public static final float GHOST_CHUCKLE_VOLUME = .8f;
    public static final float GAME_MUSIC_VOLUME = .4f;
    
    //Sets the delay in milliseconds between ghostChuckle.play() and gameMusic.loop()
    public static final float START_SOUND_MUSIC_DELAY = 900;
}
