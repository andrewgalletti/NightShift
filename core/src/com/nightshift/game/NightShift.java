package com.nightshift.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nightshift.game.data.Constants;
import com.nightshift.game.data.ScreenData;
import com.nightshift.game.screens.*;
import com.nightshift.game.sprites.LifeBar;

import static com.badlogic.gdx.Gdx.input;

public class NightShift extends Game {

	private Screen currentScreen;
	private ScreenData screenData;
	public LifeBar health;

	public OrthographicCamera camera;

	public void create() {
		screenData = new ScreenData();
		health = new LifeBar(this);
		currentScreen = new StartScreen(this);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
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
			currentScreen = new GameScreen(this,(index + 1) % 4);
		}
		if(currentScreen instanceof GameOverScreen || currentScreen instanceof SuccessScreen) {
			currentScreen = new GameScreen(this, 0);
			health = new LifeBar(this);
		}

	}

	public void endGame() {
		currentScreen = new GameOverScreen(this);
		health = new LifeBar(this);
	}

	public void success() {
		currentScreen = new SuccessScreen(this);
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