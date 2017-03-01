package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Janitor extends Sprite {

	public static final int SPEED = 600;

	private static Texture img = new Texture(Gdx.files.internal("Stand.png"));
	private Body body;
	private World world;

	public PlayerDirection direction = PlayerDirection.UP;
	public Vector2 velocity = new Vector2(0,0);

	public Janitor(int xPos, int yPos, World world) {
		super(img,img.getWidth(), img.getHeight());
		this.setX(xPos);
		this.setY(yPos);
		this.world = world;
		createPhysicsBody();
	}

	public void moveJanitor(int input) {
		switch(input) {
			case(Input.Keys.RIGHT):
				velocity.x += SPEED;
				direction = PlayerDirection.RIGHT;
				break;
			case(Input.Keys.LEFT):
				velocity.x -= SPEED;
				direction = PlayerDirection.LEFT;
				break;
			case(Input.Keys.UP):
				velocity.y += SPEED;
				direction = PlayerDirection.UP;
				break;
			case(Input.Keys.DOWN):
				velocity.y -= SPEED;
				direction = PlayerDirection.DOWN;
				break;
			default:
				break;
		}
		this.body.setLinearVelocity(velocity);
	}

	public void updateSpritePos() {
		this.setX(body.getPosition().x);
		this.setY(body.getPosition().y);
	}

	public void resetVelocity() {
		velocity.x = 0;
		velocity.y = 0;
		body.setLinearVelocity(velocity);
	}

	public Body getBody() {
		return this.body;
	}

	private void createPhysicsBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(this.getX(), this.getY());

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(this.getWidth()/2, this.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = .5f;
		fixtureDef.density = .1f;

		this.body = this.world.createBody(bodyDef);
		this.body.createFixture(fixtureDef);
	}
}