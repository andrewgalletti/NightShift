package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartScreen implements Screen {

    NightShift game;
    OrthographicCamera camera;
    SpriteBatch batch = new SpriteBatch();
    BitmapFont font1 = new BitmapFont(Gdx.files.internal("font/white32.fnt"));
    BitmapFont font2 = new BitmapFont(Gdx.files.internal("font/white64.fnt"));
    public StartScreen(NightShift game) {
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
        font2.draw(batch, "NightShift", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*2/3);
        font1.draw(batch, "Tap anywhere to begin!", 100, 200);
        batch.end();

        if(Gdx.input.isTouched()) {
            game.setScreen();
            dispose();
        }
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
}
