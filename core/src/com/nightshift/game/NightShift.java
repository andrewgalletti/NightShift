package com.nightshift.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;

public class NightShift extends ApplicationAdapter {
	private SpriteBatch batch;
	private World world;
	private Janitor hero;
	private ArrayList<Ghost> enemies;
	private TiledMap map;
	private Vector3 center;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer layer0;

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
		hero.update();
		for(Ghost g: enemies) {
			g.moveGhost();
		}
		combat();
		world.step(1f / 60f, 2, 20);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		world.step(1f / 60f, 6, 2);
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

	public void combat() {
		ArrayList<Ghost> enemiesWithinRange = new ArrayList<Ghost>();
		for(Ghost g: enemies) {
			if(hero.isGhostInHitBox(g))
				enemiesWithinRange.add(g);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			System.out.println("Pressed Space");
			enemies.removeAll(enemiesWithinRange);
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
				if(janitorOnGhost(b1,b2)) {
					hero.takeDamage();
					if(hero.isDead())
						System.exit(0);
				}
				else {
					//if(ghostOnGhost(b1,b2))
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

	private boolean ghostOnGhost(Body b1, Body b2) {
		boolean b1IsGhost = false;
		for(Ghost g: enemies) {
			if(b1 == g.getBody()) {
				b1IsGhost = true;
			}
		}
		if(b1IsGhost) {
			for(Ghost g: enemies) {
				if(b2 == g.getBody())
					return true;
			}
		}
		return false;
	}
}