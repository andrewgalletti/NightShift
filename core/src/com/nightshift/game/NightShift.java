package com.nightshift.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class NightShift extends ApplicationAdapter {
	private SpriteBatch batch;
	private World world;
	private Janitor hero;
	private Ghost[] enemies;
	private TiledMap map;
	private Vector3 center;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer layer0;
//	public static Texture backgroundTexture;

	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		world = new World(new Vector2(0, 0), true);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		map = new TmxMapLoader().load("mymap.tmx");
		map.getProperties();
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		layer0 = (TiledMapTileLayer) map.getLayers().get(0);
		center = new Vector3(layer0.getWidth() * layer0.getTileWidth() / 2, layer0.getHeight() * layer0.getTileHeight() / 2, 0);
		camera.position.set(center);

		batch = new SpriteBatch();
		hero = new Janitor(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, world);
		spawnEnemies();

		this.initContactListener();
	}

	@Override
	public void render() {
		hero.moveJanitor();
		world.step(1f / 60f, 2, 20);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		world.step(1f / 60f, 6, 2);
		hero.updateJanitorPosition();
		for(Ghost g: enemies) {
			//g.moveGhost();
			g.patrol();
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
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	private void spawnEnemies() {
		enemies = new Ghost[4];
		enemies[0] = new Ghost(hero, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4, world);
		enemies[1] = new Ghost(hero, Gdx.graphics.getWidth() * 3 / 4, Gdx.graphics.getHeight() / 4, world);
		enemies[2] = new Ghost(hero, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() * 3 / 4, world);
		enemies[3] = new Ghost(hero, Gdx.graphics.getWidth() * 3 / 4, Gdx.graphics.getHeight() * 3 / 4, world);
	}

	private void initContactListener() {
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {System.out.println("Contact began.");}

			@Override
			public void endContact(Contact contact) {System.out.println("Contact ended.");
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
		});
	}
}
