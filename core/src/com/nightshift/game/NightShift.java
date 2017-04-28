package com.nightshift.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.nightshift.game.screens.GameOverScreen;
import com.nightshift.game.screens.GameScreen;
import com.nightshift.game.screens.PauseScreen;
import com.nightshift.game.screens.StartScreen;
import com.nightshift.game.sprites.LifeBar;

import static com.badlogic.gdx.Gdx.input;

public class NightShift extends Game {

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
			//You aaaare the only exception.
		}
	}

	public void checkPause() {
		if(input.isKeyPressed(Input.Keys.ESCAPE)) {
			currentScreen = new PauseScreen(this);
		}
	}
}