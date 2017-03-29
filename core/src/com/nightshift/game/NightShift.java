package com.nightshift.game;
import com.badlogic.gdx.*;

public class NightShift extends ApplicationAdapter {

	private GameScreen[] levels;
	private int currentLevel;
	private Screen currentScreen;

	@Override
	public void create() {
		levels = new GameScreen[3];
		levels[0] = new GameScreen("mymap.tmx");
		levels[1] = new GameScreen("mymap.tmx");
		levels[2] = new GameScreen("mymap.tmx");
	}

	@Override
	public void render() {
		levels[0].render(0);
	}

	@Override
	public void dispose() {}

}