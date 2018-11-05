package com.nightshift.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nightshift.game.NightShift;
import com.nightshift.game.data.Constants;

import java.util.ArrayList;

public class StartScreen implements Screen {

    private NightShift game;
    private OrthographicCamera camera;
    private Viewport port;
    private Stage stage;
    private ArrayList<Image> listOfImages;
    private boolean firstCall;
    SpriteBatch batch = new SpriteBatch();

    Texture playButton = new Texture(Gdx.files.internal("ButtonImg/PlayButton.png"));
    Texture levelButton = new Texture(Gdx.files.internal("ButtonImg/LevelButton.png"));
    Texture ghostImg = new Texture(Gdx.files.internal("ButtonImg/ghost.png"));

    public StartScreen(NightShift game) {

        this.game = game;
        camera = new OrthographicCamera();
        setup();
        setupImg();
        InputProcessor();
        camera.setToOrtho(false, 800, 480);

    }

    private void setup() {
        camera = new OrthographicCamera();
        port = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(port.getWorldWidth(), port.getWorldHeight(), 0);
        stage = new Stage(port);
        firstCall = true;
    }

    /**
     *
     *
     **/
    private void setupImg(){

        listOfImages = new ArrayList<Image>();
        listOfImages.add(new Image(playButton));
        listOfImages.add(new Image(levelButton));

    }


    private void InputProcessor() {

        Gdx.input.setInputProcessor(stage);

        int multiplier = 4;
        for (final Image img: listOfImages) {
            img.setSize(img.getWidth() / 3.5f, img.getHeight() / 1.7f);
            img.setPosition(Constants.VIEWPORT_WIDTH/1.9f - (img.getWidth() / 2), (Constants.VIEWPORT_HEIGHT / 6f)  * multiplier - (img.getHeight() / 2)); //(port.getWorldWidth() / 2) - (img.getWidth() / 2), (port.getWorldHeight() / 6) * multiplier - (img.getHeight() / 2)
            multiplier--;

            stage.addActor(img);

            img.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    //img.setColor(Color.BLUE);
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    //img.setColor(Color.WHITE);
                    if (img == listOfImages.get(0)) {
                        firstCall = false;
                        game.setScreen(1);
                    }  else if (img == listOfImages.get(1)) {
                        game.setScreen();
                    }
                }
            });
        }

    }

    @Override
    public void render(float delta) {

        //Sets background color
        Gdx.gl.glClearColor(0.3f, 0.6f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(ghostImg, Constants.VIEWPORT_WIDTH/30 , Constants.VIEWPORT_HEIGHT/30 );
        batch.end();


        stage.draw();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

    }

    @Override
    public void show(){

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
