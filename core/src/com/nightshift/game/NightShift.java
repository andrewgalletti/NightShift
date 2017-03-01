package com.nightshift.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class NightShift extends ApplicationAdapter implements InputProcessor {
	
	private SpriteBatch batch;
	private World world;
	private Janitor hero;
	private Ghost villain;
//	public static Texture backgroundTexture;
	
	@Override
	public void create() {
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();

		hero = new Janitor(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, world);
		villain = new Ghost(hero,Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3, world);

		this.initContactListener();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		world.step(1f/60f, 6, 2);
		hero.updateSpritePos();
		villain.moveGhost();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		backgroundTexture = new Texture("mymap.tmx");
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



	@Override
	public boolean keyDown(int keycode) {
		hero.moveJanitor(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		hero.resetVelocity();
		return false;
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
