package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;


import java.util.ArrayList;

public class GameScreen implements Screen {

    //MapData returns enemy spawn locations and map file name based on a level index.
    private static MapData mapData = new MapData();
    private int levelIndex;

    //Provides a reference to NightShift for access to LifeBar and to setScreen() method.
    private NightShift game;
    private Janitor hero;
    //World used for Box2d physics.
    private World world;
    //Used to draw Sprite objects on the screen.
    private SpriteBatch batch;
    private ArrayList<Ghost> enemies;
    private Vector3 center;
    private OrthographicCamera camera;
    private TiledMap map;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMapTileLayer mapTileLayer;
    //Map layer that contains the end location stored as a RectangleMapObject.
    private MapLayer winLayer;
    //Map layer that contains the walls in MapObjects.
    private MapLayer wallLayer;
    //Object that stores the walls as an array of RectangleMapObject.
    private MapObjects mapObjects;
    private FitViewport fitViewport;
    private Sprite aspectRatios;

    public GameScreen(NightShift game, int levelIndex) {
        this.game = game;
        //Initializes a world with no gravity.
        this.world = new World(new Vector2(0, 0), true);
        this.levelIndex = levelIndex;

        map = new TmxMapLoader().load(mapData.getFileName(levelIndex));
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        mapTileLayer = (TiledMapTileLayer) map.getLayers().get(0);
        wallLayer = map.getLayers().get(1);
        winLayer = map.getLayers().get(2);
        mapObjects = wallLayer.getObjects();

        Gdx.graphics.setWindowedMode(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        //Gdx.graphics.setWindowedMode((int)mapData.previousScreenDimensions.x,(int)mapData.previousScreenDimensions.y);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        //camera.setToOrtho(false, (int)mapData.previousScreenDimensions.x,(int)mapData.previousScreenDimensions.y);
        camera.update();
        fitViewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
        //fitViewport = new FitViewport((int)mapData.previousScreenDimensions.x,(int)mapData.previousScreenDimensions.y, camera);

        //fitViewport.apply();
        //center = new Vector3(mapTileLayer.getWidth() * mapTileLayer.getTileWidth() / 2,
        //mapTileLayer.getHeight() * mapTileLayer.getTileHeight() / 2, 0)
        //camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        //FIGURE OUT WHAT THE HELL THIS ALL DOES

        this.hero = new Janitor(45, 45, this);
        this.batch = new SpriteBatch();
        spawnEnemies();
    }

    @Override
    public void render(float delta) {
        hero.moveJanitor();
        hero.updateTimers();
        for(Ghost g: enemies) {
            g.moveGhost();
        }

        world.step(1f / 60f, 6, 2);
        checkWin();
        checkGhostCollisions();

        hero.updateJanitorPosition();
        for(Ghost g: enemies) {
            g.updateGhostPosition();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();
        game.health.draw(batch);
        hero.draw(batch);
        for(Ghost g: enemies) {
            g.draw(batch);
        }
        batch.end();

        hero.resetVelocity();
        for(Ghost g: enemies) {
            g.resetVelocity();
        }
    }

    private void spawnEnemies() {
        enemies = new ArrayList<Ghost>();
        for(Vector2 pos: mapData.getEnemies(levelIndex)) {
            enemies.add(new Ghost(hero, pos.x, pos.y, world));
        }
    }

    public boolean mapCollisionWillOccur() {
        int threshold = 10;
        Rectangle player = new Rectangle(0,0,0,0);

        switch(hero.getDirection()) {
            case BACK:
                player = new Rectangle(hero.getX(),hero.getY(),hero.getDimensions().x,hero.getDimensions().y + threshold);
                break;
            case LEFT:
                player = new Rectangle(hero.getX() - threshold + 6,hero.getY(),hero.getDimensions().x,hero.getDimensions().y);
                break;
            case FRONT:
                player = new Rectangle(hero.getX(),hero.getY() - threshold + 6,hero.getDimensions().x,hero.getDimensions().y);
                break;
            case RIGHT:
                player = new Rectangle(hero.getX(),hero.getY(),hero.getDimensions().x + threshold,hero.getDimensions().y);
                break;
        }

        for(RectangleMapObject r: mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = r.getRectangle();
            if(Intersector.overlaps(player,rect))
                return true;
        }

        return false;
    }

    public void checkGhostCollisions() {
        Rectangle player = new Rectangle(hero.getX(),hero.getY(),hero.getDimensions().x,hero.getDimensions().y);
        for(Ghost g: enemies) {
            Rectangle ghost = new Rectangle(g.getX(),g.getY(),g.getWidth(),g.getHeight());
            if(Intersector.overlaps(player,ghost)) {
                hero.takeDamage(game.health);
                return;
            }
        }
    }

    private void checkWin() {
        Rectangle player = new Rectangle(hero.getX(),hero.getY(),hero.getDimensions().x,hero.getDimensions().y);
        for(RectangleMapObject r: winLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = r.getRectangle();
            if(Intersector.overlaps(player,rect)) {
                mapData.previousScreenDimensions = new Vector2(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
                game.setScreen();
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public int getLevelIndex() {
        return this.levelIndex;
    }

    @Override
    public void show() {}
    public void resize(int width, int height) {
        fitViewport.update(width,height);
        //camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }
    public void pause() {}
    public void resume() {}
    public void hide() {}
    public void dispose() {}
}

