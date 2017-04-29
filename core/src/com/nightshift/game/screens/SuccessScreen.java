package com.nightshift.game.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nightshift.game.NightShift;
import com.nightshift.game.data.Constants;

/**
 * Created by Cui on 4/29/2017.
 */
public class SuccessScreen implements Screen{
    NightShift game;
    OrthographicCamera camera;
    SpriteBatch batch = new SpriteBatch();
    BitmapFont font1 = new BitmapFont(Gdx.files.internal("font/white32.fnt"));
    BitmapFont font2 = new BitmapFont(Gdx.files.internal("font/white64.fnt"));

    public SuccessScreen(NightShift game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.3f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font2.draw(batch, "You have escaped!", 50, Constants.VIEWPORT_HEIGHT/2 + 100);
        font1.draw(batch, "Press any key to restart!", Constants.VIEWPORT_WIDTH/2 - 80, Constants.VIEWPORT_HEIGHT/2 - 50);
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.setScreen();
            dispose();
        }
    }
    public void resize(int width, int height) {

    }
    public void pause() {

    }
    public void resume() {

    }
    public void hide() {

    }
    public void dispose() {

    }

}
