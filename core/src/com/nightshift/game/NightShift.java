package com.nightshift.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
	private Janitor hero, villan;
	
	@Override
	public void create() {
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();
		hero = new Janitor(0, 389, world);
		villan = new Janitor(0, 0, world);
		this.initContactListener();
	}

	@Override
	public void render() {
		world.step(1f/60f, 6, 2);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		hero.savePos();
		hero.moveJanitor();
		batch.begin();
		hero.draw(batch);
		villan.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose() {
		hero.getTexture().dispose();
		batch.dispose();
	}
	
	public void initContactListener() {
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				System.out.println("Contact began.");
				if((contact.getFixtureA().getBody() == hero.getBody()&&contact.getFixtureB().getBody() == villan.getBody())||(contact.getFixtureA().getBody() == villan.getBody()&&contact.getFixtureB().getBody() == hero.getBody())){
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
