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

public class NightShift extends ApplicationAdapter {
	
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
		villain = new Ghost(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3, world);

		this.initContactListener();
	}

	@Override
	public void render() {
		world.step(1f/60f, 6, 2);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		backgroundTexture = new Texture("mymap.tmx");

		//hero.moveJanitor();
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
