package com.nightshift.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;

import static com.badlogic.gdx.Gdx.input;

public class NightShift extends Game {

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 400;
	private Screen currentScreen;
	public LifeBar health;

	public void create() {
		health = new LifeBar(this);
		currentScreen = new StartScreen(this);
	}

	@Override
	public void render() {
		checkPause();
		currentScreen.render(Gdx.graphics.getDeltaTime());
		/*if(currentScreen instanceof GameScreen || currentScreen instanceof PauseScreen)
			currentScreen.render(Gdx.graphics.getDeltaTime());
		else {
			super.render();
		}*/
	}

	@Override
	public void dispose() {

	}

	public void setScreen() {
		if(currentScreen instanceof StartScreen) {
			currentScreen = new GameScreen(this,0);
			return;
		}
		if(currentScreen instanceof GameScreen) {
			int index = ((GameScreen) currentScreen).getLevelIndex();
			currentScreen = new GameScreen(this,(index + 1) % 3);
		}
		if(currentScreen instanceof GameOverScreen) {
			currentScreen = new GameScreen(this, 0);
		}
	}

	public void endGame() {
		currentScreen = new GameOverScreen(this);
		health = new LifeBar(this);
	}

	public void setScreen(int level) {
		try {
			currentScreen = new GameScreen(this, level);
			System.out.println("Changed Levels to: " + level);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			//
		}
	}

	public void checkPause() {
		if(input.isKeyPressed(Input.Keys.ESCAPE)) {
			currentScreen = new PauseScreen(this);
		}
	}
}