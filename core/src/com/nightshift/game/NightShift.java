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
	private Janitor hero, villain;
	private Ghost[] enemies;
	
	@Override
	public void create() {
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();
		hero = new Janitor(0, 389+100, world);
		villain = new Janitor(0, 0, world);
		spawnEnemies();
		this.initContactListener();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		//for(Ghost g: enemies)
			//g.moveGhost();
		world.step(1f/60f, 6, 2);
		//for(Ghost g: enemies)
			//g.updateSpritePos();
		hero.updateSpritePos();
		villain.updateSpritePos();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		hero.draw(batch);
		//for(Ghost g: enemies)
			//g.draw(batch);
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
		System.out.println("Pressed down on key");
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		System.out.println("Let go of key");
		hero.resetVelocity();
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void spawnEnemies() {
		//enemies = new Ghost[1];
		//for(int i = 1; i <= 1; i++)
		//	enemies[i-1] = new Ghost(350*i+300,100,new Vector2(350*i+300,100),world);
		//enemies[0] = new Ghost(400,10);
	}

	public void initContactListener() {
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if((contact.getFixtureA().getBody() == hero.getBody()&&contact.getFixtureB().getBody() == villain.getBody())||(contact.getFixtureA().getBody() == villain.getBody()&&contact.getFixtureB().getBody() == hero.getBody())){
					System.out.println("CONTACT");
					hero.bounce();
				}
			}

			@Override
			public void endContact(Contact contact) {}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
	}
}
