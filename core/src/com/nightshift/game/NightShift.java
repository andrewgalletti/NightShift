package com.nightshift.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;


public class NightShift extends Game {

	private Screen currentScreen;
	private StartScreen start;
	private GameScreen[] levels;

	public void create() {
		start = new StartScreen(this);
		levels = new GameScreen[1];
		levels[0] = new GameScreen("mymap.tmx");

		currentScreen = start;
	}

	@Override
	public void render() {
		currentScreen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {

	}

	public void setScreen() {
		if(currentScreen instanceof StartScreen) {
			currentScreen = levels[0];
		}
	}
}