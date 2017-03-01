package com.nightshift.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.Graphics.DisplayMode;

<<<<<<< HEAD

public class NightShift extends ApplicationAdapter{
=======
public class NightShift extends ApplicationAdapter implements InputProcessor {
>>>>>>> origin/master
	
	private SpriteBatch batch;
	private World world;
	private Janitor hero;
	private Ghost villain;
<<<<<<< HEAD

=======
	private TiledMap map;
	private Vector3 center;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMapTileLayer layer0;
>>>>>>> origin/master
//	public static Texture backgroundTexture;

	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		world = new World(new Vector2(0, 0), true);
<<<<<<< HEAD
=======
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();

		map = new TmxMapLoader().load("mymap.tmx");
		map.getProperties();
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		layer0 = (TiledMapTileLayer) map.getLayers().get(0);
		center = new Vector3(layer0.getWidth()*layer0.getTileWidth()/2, layer0.getHeight()*layer0.getTileHeight()/2,0);
		camera.position.set(center);
>>>>>>> origin/master
		batch = new SpriteBatch();
		hero = new Janitor(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, world);
		villain = new Ghost(hero,Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3, world);

		this.initContactListener();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		world.step(1f/60f, 2, 20);
		Gdx.gl.glClearColor(1,0,0,1);
		Gdx.gl.glBlendFunc(GL20.GL_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
		world.step(1f/60f, 6, 2);
		hero.updateSpritePos();
		villain.moveGhost();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
//		backgroundTexture = new Texture("mymap.tmx");
<<<<<<< HEAD
		hero.savePos();
		hero.moveJanitor();
//		villain.wander();
		follow();
=======
>>>>>>> origin/master
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

<<<<<<< HEAD
	public void follow() {
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
=======


	@Override
	public boolean keyDown(int keycode) {
		hero.moveJanitor(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		hero.resetVelocity();
		return false;
>>>>>>> origin/master
	}

	@Override
	public boolean keyTyped(char character) {return false;}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	@Override
	public boolean scrolled(int amount) {return false;}
	
	public void initContactListener() {
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				System.out.println("Contact began.");
				if((contact.getFixtureA().getBody() == hero.getBody()&&contact.getFixtureB().getBody() == villain.getBody())||(contact.getFixtureA().getBody() == villain.getBody()&&contact.getFixtureB().getBody() == hero.getBody())){
					System.out.println("Boolean expression evaluated true.");
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
