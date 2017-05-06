package com.nightshift.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.nightshift.game.data.Constants;
import com.nightshift.game.screens.*;
import com.nightshift.game.sprites.LifeBar;

import static com.badlogic.gdx.Gdx.input;

public class NightShift extends Game {

	//Pointer used to denote the current screen to be displayed on this iteration of render().
	private Screen currentScreen;

	//Same sound as LifeBar.takeDamage played before gameMusic, as it sounded too sudden without the ghostChuckle.
	private Sound ghostChuckle;
	private Sound gameMusic;

	//Used to store user's lives across different levels, as well as, initiate the end sequence if the user runs out of lives.
	public LifeBar health;
	//TODO: define camera
	public OrthographicCamera camera;

	private long timeOfStartSound = 0;
	private boolean musicCurrentlyLooping = false;

	public void create() {
		health = new LifeBar(this);
		currentScreen = new StartScreen(this);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		startSound();
	}

	@Override
	public void render() {
		startMusic();
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
		gameMusic.dispose();
		ghostChuckle.dispose();
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

	private void checkPause() {
		if(input.isKeyPressed(Input.Keys.ESCAPE)) {
			currentScreen = new PauseScreen(this);
		}
	}

	private void startSound() {
		gameMusic = Gdx.audio.newSound(Gdx.files.internal("Sounds/NightShift_LONG.mp3"));
		ghostChuckle = Gdx.audio.newSound(Gdx.files.internal("Sounds/TakeDamage.mp3"));
		ghostChuckle.play(Constants.GHOST_CHUCKLE_VOLUME);
		timeOfStartSound = System.currentTimeMillis();
	}

	//Starts looping gameMusic after ghostChuckle and appropriate delay.
	private void startMusic() {
		if(!musicCurrentlyLooping && System.currentTimeMillis() - timeOfStartSound >= Constants.START_SOUND_MUSIC_DELAY) {
			gameMusic.loop(Constants.GAME_MUSIC_VOLUME);
			musicCurrentlyLooping = true;
		}
	}
}