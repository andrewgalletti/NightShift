package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import static com.badlogic.gdx.Gdx.input;

public class Janitor {
    private final float SPEED = 100;
    private final int ANIMATION_FACTOR = 4;

    public int lives = 3;
    private int moveIterCounter = 0;

    private World world;
    private Body body;
    private Sprite currentSprite;
    private Sprite[] animation;
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);

    public Janitor(int xPos, int yPos, World world) {
        position.x = xPos;
        position.y = yPos;
        this.world = world;
        initSpriteArray();
        currentSprite = animation[0];
        createPhysicsBody();
    }

    public void moveJanitor() {
        boolean didMove = false;
        if(input.isKeyPressed(Input.Keys.UP)) {
            velocity.y += SPEED;
            didMove = true;
        }
        if(input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y -= SPEED;
            didMove = true;
        }
        if(input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x += SPEED;
            didMove = true;
        }
        if(input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x -= SPEED;
            didMove = true;
        }

        if(didMove) {
            moveIterCounter++;
            currentSprite = animation[moveIterCounter/ANIMATION_FACTOR%animation.length];
        }

        body.setLinearVelocity(velocity);
    }

    public void updateJanitorPosition() {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        updateSpritePositions();
    }

    private void updateSpritePositions() {
        for (Sprite s: animation) {
            s.setX(position.x);
            s.setY(position.y);
        }
    }

    public void resetVelocity() {
        velocity.x = 0;
        velocity.y = 0;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void draw(SpriteBatch batch) {
        currentSprite.draw(batch);
    }

    private void initSpriteArray() {
        Texture t0 = new Texture(Gdx.files.internal("Stand.png"));
        Texture t1 = new Texture(Gdx.files.internal("Left Step.png"));
        Texture t2 = new Texture(Gdx.files.internal("Stand.png"));
        Texture t3 = new Texture(Gdx.files.internal("Right Step.png"));
        animation = new Sprite[4];
        animation[0] = new Sprite(t0,t0.getWidth(),t0.getHeight());
        animation[1] = new Sprite(t1,t1.getWidth(),t1.getHeight());
        animation[2] = new Sprite(t2,t2.getWidth(),t2.getHeight());
        animation[3] = new Sprite(t3,t3.getWidth(),t3.getHeight());
        updateSpritePositions();
    }

    private void createPhysicsBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(currentSprite.getX(), currentSprite.getY());

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(currentSprite.getWidth()/2, currentSprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = .5f;
        fixtureDef.density = .1f;

        this.body = this.world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
    }

    public Body getBody() {
        return this.body;
    }
}

