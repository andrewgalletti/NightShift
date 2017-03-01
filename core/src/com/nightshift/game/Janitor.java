package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Janitor extends Sprite {
	
	public static final int STEP_SIZE = 10;
	
	private static Texture img = new Texture(Gdx.files.internal("Mr. Clean.png"));
	private Body body;
	private World world;
	private float prevX, prevY;
	private boolean spriteFlip = false;
	
	public Janitor(int xPos, int yPos, World world) {
		super(img,img.getWidth(), img.getHeight());
		this.setX(xPos);
		this.setY(yPos);
		this.world = world;
		prevX = xPos;
		prevY = yPos;
		createPhysicsBody();
	}
	
	public void moveJanitor() {
		boolean didMove = false;
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.translateX(STEP_SIZE);
			this.body.setTransform(this.getX(), this.getY(), 0);
			this.setFlip(spriteFlip, false);
			didMove = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.translateX(-STEP_SIZE);
			this.body.setTransform(this.getX(), this.getY(), 0);
			this.setFlip(spriteFlip, false);
			didMove = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) { 
			this.translateY(STEP_SIZE);
			this.body.setTransform(this.getX(), this.getY(), 0);
			this.setFlip(spriteFlip, false);
			didMove = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.translateY(-STEP_SIZE);
			this.body.setTransform(this.getX(), this.getY(), 0);
			this.setFlip(spriteFlip, false);
			didMove = true;
		}
		if(didMove) {
			spriteFlip = !spriteFlip;
		}
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
        
        this.body = this.world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
	}
	
	public void savePos() {
		prevX = this.getX();
		prevY = this.getY();
	}
	
	public void moveToPreviousPosition() {
		this.body.setTransform(prevX, prevY, 0);
		this.setX(prevX);
		this.setY(prevY);
		System.out.println("Sprite was moved back.");
	}
}
