package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

import java.util.ArrayList;

public class GameScreen implements Screen {

    private static MapData mapData;

    private Janitor hero;
    private World world;
    private SpriteBatch batch;
    private ArrayList<Ghost> enemies;
    private TiledMap map;
    private Vector3 center;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMapTileLayer mapTileLayer;
    private MapLayer wallLayer;
    private MapObjects mapObjects;

    private int levelIndex;

    public GameScreen(int levelIndex) {
        mapData = new MapData();
        this.world = new World(new Vector2(0, 0), true);
        this.levelIndex = levelIndex;

        map = new TmxMapLoader().load(mapData.getFileName(levelIndex));
        map.getProperties();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        mapTileLayer = (TiledMapTileLayer) map.getLayers().get(0);
        wallLayer = map.getLayers().get(1);
        mapObjects = wallLayer.getObjects();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        center = new Vector3(mapTileLayer.getWidth() * mapTileLayer.getTileWidth() / 2,
                mapTileLayer.getHeight() * mapTileLayer.getTileHeight() / 2, 0);
        camera.position.set(center);

        batch = new SpriteBatch();
        this.hero = new Janitor(45, 45, this);
        spawnEnemies();
    }

    @Override
    public void render(float delta) {
        hero.moveJanitor();
        hero.updateTimers();
        combat();

        for(Ghost g: enemies) {
            g.moveGhost();
        }
        world.step(1f / 60f, 6, 2);
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

    public void combat() {
        ArrayList<Ghost> enemiesWithinRange = new ArrayList<Ghost>();
        for(Ghost g: enemies) {
            if(hero.isGhostInHitBox(g))
                enemiesWithinRange.add(g);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && hero.readyToAttack()) {
            hero.applyAttackDelay();
            for(Ghost g: enemiesWithinRange) {
                g.lives--;
                if(g.lives < 1)
                    enemies.remove(g);
            }
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
                hero.takeDamage();
                return;
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
    public void resize(int width, int height) {}
    public void pause() {}
    public void resume() {}
    public void hide() {}
    public void dispose() {}
}
