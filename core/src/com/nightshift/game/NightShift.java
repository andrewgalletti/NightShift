package com.nightshift.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class NightShift extends ApplicationAdapter {
	
	private SpriteBatch batch;
	private World world;
	private Janitor hero;
	private Ghost villain;
	private TiledMap map;
	private OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
//	public static Texture backgroundTexture;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		world = new World(new Vector2(0, 0), true);
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();
		map = new TmxMapLoader().load("mymap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		batch = new SpriteBatch();
		hero = new Janitor(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, world);
		villain = new Ghost(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3, world);

//		this.initContactListener();
	}

	@Override
	public void render() {
		world.step(1f/60f, 2, 20);
		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glBlendFunc(GL20.GL_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
//		backgroundTexture = new Texture("mymap.tmx");
		hero.savePos();
		hero.moveJanitor();
//		villain.wander();
		move();
		batch.begin();
		hero.draw(batch);
		villain.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		hero.getTexture().dispose();
		batch.dispose();
	}

	public void move() {
		float targetX = hero.getX(); //Player's position
		float targetY = hero.getY();
		float spriteX = villain.getX(); //Ghost's
		float spriteY = villain.getY();
		float x2 = villain.getX(); //Ghost's new position
		float y2 = villain.getY();
		float angle;
		angle = (float) Math.atan2(targetY - spriteY, targetX - spriteX);
		x2 += (float) Math.cos(angle) * 125 * Gdx.graphics.getDeltaTime();
		y2 += (float) Math.sin(angle) * 125 * Gdx.graphics.getDeltaTime();
		villain.setPosition(x2, y2); //Set enemy's new positions.
	}
	
	public void initContactListener() {
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				System.out.println("Contact began.");
				if((contact.getFixtureA().getBody() == hero.getBody()&&contact.getFixtureB().getBody() == villain.getBody())||(contact.getFixtureA().getBody() == villain.getBody()&&contact.getFixtureB().getBody() == hero.getBody())){
					System.out.println("Boolean expression evaluated true.");
					hero.moveToPreviousPosition();
				}
			}

			@Override
			public void endContact(Contact contact) {
				System.out.println("Contact ended.");
			}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
	}
}
