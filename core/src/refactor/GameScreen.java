package refactor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import refactor.Ghost;
import refactor.Janitor;
import com.nightshift.game.Menu;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.ArrayList;

/**
 * Created by Cui on 3/28/2017.
 */
public class GameScreen implements Screen {
    public SpriteBatch batch;
    private World world;

    private ArrayList<Ghost> enemies;
    private TiledMap map;
    private Vector3 center;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMapTileLayer mapTileLayer;
    private MapLayer objectLayer;
    private MapObjects mapObjects;
    private String livesDisplay;
    private Menu screen;
    BitmapFont font;
    private static NightShift game;
    private Janitor hero;
    private Stage stage;
    private Texture heroImg;

    public GameScreen(NightShift game) {
        GameScreen.game = game;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        world = new World(new Vector2(0, 0), true);
        hero = new Janitor(this);
        map = new TmxMapLoader().load("mymap.tmx");
        map.getProperties();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        mapTileLayer = (TiledMapTileLayer) map.getLayers().get(0);
        objectLayer = map.getLayers().get(1);
        mapObjects = objectLayer.getObjects();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        center = new Vector3(mapTileLayer.getWidth() * mapTileLayer.getTileWidth() / 2, mapTileLayer.getHeight() * mapTileLayer.getTileHeight() / 2, 0);
        camera.position.set(center);

        batch = new SpriteBatch();
        spawnEnemies();

        font = new BitmapFont();
        livesDisplay = "Remaining lives: " + hero.lives;

        this.initContactListener();
    }

    public void render(float delta) {
        hero.moveJanitor();
        hero.update();
        for(Ghost g: enemies) {
            g.moveGhost();
        }
        combat();
        world.step(1f / 60f, 2, 20);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        world.step(1f / 60f, 6, 2);
        if(mapCollisionDidOccur())
            hero.revertBodyPosition();
        hero.updateJanitorPosition();
        for(Ghost g: enemies) {
            g.updateGhostPosition();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, livesDisplay, 25, Gdx.graphics.getHeight() - 10);
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

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void combat() {
        ArrayList<Ghost> enemiesWithinRange = new ArrayList<Ghost>();
        for(Ghost g: enemies) {
            if(hero.isGhostInHitBox(g))
                enemiesWithinRange.add(g);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("Pressed Space");
            for(Ghost g: enemiesWithinRange) {
                g.lives--;
                if(g.lives < 1)
                    enemies.remove(g);
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void spawnEnemies() {
        enemies = new ArrayList<Ghost>();
        enemies.add(new Ghost(hero, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, world));
        enemies.add(new Ghost(hero, Gdx.graphics.getWidth() * 3 / 4, Gdx.graphics.getHeight() / 4, world));
        enemies.add(new Ghost(hero, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() * 3 / 4, world));
        enemies.add(new Ghost(hero, Gdx.graphics.getWidth() * 3 / 4, Gdx.graphics.getHeight() * 3 / 4, world));
    }

    private void initContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body b1 = contact.getFixtureA().getBody();
                Body b2 = contact.getFixtureB().getBody();
                if(janitorOnGhost(b1,b2) && (System.currentTimeMillis() % 6 == 0)) {
                    hero.lives--;
                    livesDisplay = "Remaining lives: " + hero.lives;
                    if(hero.isDead())
                        System.exit(0);
                }
                else {
                    ghostOnGhost(b1,b2);
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    private boolean janitorOnGhost(Body b1, Body b2) {
        for(Ghost g: enemies) {
            if((b1 == hero.getBody()&& b2 == g.getBody()||(b1 == g.getBody() && b2 == hero.getBody()))) {
                return true;
            }
        }
        return false;
    }

    private void ghostOnGhost(Body b1, Body b2) {
        Ghost ghost = null;
        for(Ghost g1: enemies) {
            if(b1 == g1.getBody()) {
                ghost = g1;
                break;
            }
        }
        if(ghost != null) {
            for(Ghost g2: enemies) {
                if(b2 == g2.getBody()) {
                    ghost.mergeGhosts(g2);
                    enemies.remove(g2);
                    return;
                }
            }
        }
    }

    public boolean mapCollisionWillOccur() {
        Rectangle player = new Rectangle(0,0,0,0);
        switch(hero.getDirection()) {
            case BACK:
                player = new Rectangle(hero.getX(),hero.getY()+1,hero.getDimensions().x,hero.getDimensions().y);
                break;
            case LEFT:
                player = new Rectangle(hero.getX()-1,hero.getY(),hero.getDimensions().x,hero.getDimensions().y);
                break;
            case FRONT:
                player = new Rectangle(hero.getX(),hero.getY()-1,hero.getDimensions().x,hero.getDimensions().y);
                break;
            case RIGHT:
                player = new Rectangle(hero.getX()+1,hero.getY(),hero.getDimensions().x,hero.getDimensions().y);
                break;
        }

        for(RectangleMapObject r: mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = r.getRectangle();
            if(Intersector.overlaps(player,rect))
                return true;
        }

        return false;
    }

    public boolean mapCollisionDidOccur() {
        Rectangle player = new Rectangle(hero.getX(),hero.getY(),hero.getDimensions().x,hero.getDimensions().y);
        for(RectangleMapObject r: mapObjects.getByType(RectangleMapObject.class)) {
            Rectangle rect = r.getRectangle();
            if(Intersector.overlaps(player,rect))
                return true;
        }
        return false;
    }

    public World getWorld() {
        return this.world;
    }

    public void setScreen(Menu screen) {
        this.screen = screen;
    }
}
