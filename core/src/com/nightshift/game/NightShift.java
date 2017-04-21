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
	private StartScreen start;
	private PauseScreen pause;
	private Sound startMusic;

	public void create() {
		start = new StartScreen(this);
		pause = new PauseScreen(this);

		startMusic = Gdx.audio.newSound(Gdx.files.internal("Sounds/StartScreen.mp3"));
		//startMusic.loop();
		currentScreen = start;
	}

	@Override
	public void render() {
		checkPause();
		currentScreen.render(Gdx.graphics.getDeltaTime());
		/*if(currentScreen instanceof GameScreen)
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
			currentScreen = new GameScreen(0);
			return;
		}
		if(currentScreen instanceof GameScreen) {
			int index = ((GameScreen) currentScreen).getLevelIndex();
			currentScreen = new GameScreen((index + 1) % 3);
		}
	}

	public void setScreen(int level) {
		try {
			currentScreen = new GameScreen(level);
			System.out.println("Changed Levels to: " + level);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			//
		}
	}

	public void checkPause() {
		if(input.isKeyPressed(Input.Keys.ESCAPE)) {
			currentScreen = pause;
		}
	}
}