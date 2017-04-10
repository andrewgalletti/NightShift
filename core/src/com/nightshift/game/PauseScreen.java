package com.nightshift.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseScreen implements Screen {

    NightShift game;
    OrthographicCamera camera;
    SpriteBatch batch = new SpriteBatch();
    BitmapFont font = new BitmapFont();

    public PauseScreen(NightShift game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

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

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void changeLevel() {
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
    }
}
