package com.nightshift.game.data;

import com.badlogic.gdx.math.Vector2;

public class ScreenData {

    public Vector2 previousScreenDimensions;

    public ScreenData() {
        previousScreenDimensions = new Vector2(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
    }
}
