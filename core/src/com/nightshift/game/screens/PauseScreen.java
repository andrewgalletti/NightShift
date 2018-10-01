package com.nightshift.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nightshift.game.NightShift;

public class PauseScreen implements Screen {

    private NightShift game;
    private OrthographicCamera camera;
    private SpriteBatch batch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();

    public PauseScreen(NightShift game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Type Level Number", 100, 150);
        changeLevel();
        batch.end();
    }

    private void changeLevel() {
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            game.setScreen(0);
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            game.setScreen(1);
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            game.setScreen(2);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            game.setScreen(3);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            game.setScreen(4);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            game.setScreen(5);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
            game.setScreen(6);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            game.setScreen(7);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            game.setScreen(8);
        }
    }

    @Override
    public void show() {}
    public void resize(int width, int height) {}
    public void pause() {}
    public void resume() {}
    public void hide() {}
    public void dispose() {}
}
