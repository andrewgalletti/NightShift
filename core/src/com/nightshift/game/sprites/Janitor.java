package com.nightshift.game.sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nightshift.game.data.Constants;
import com.nightshift.game.data.PlayerDirection;
import com.nightshift.game.screens.GameScreen;

import static com.badlogic.gdx.Gdx.input;

public class Janitor {
    private final float SPEED = 2f;
    //Used to slow walking animation by changing sprites every 4 * 1/60ths of a second.
    private final int ANIMATION_FACTOR = 4;

    //Counts every world step that is spent in motion for purpose of animation.
    private int moveIterCounter = 0;
    private float remainingInvulnerability;

    private GameScreen game;
    private World world;
    //Physical representation of the Janitor used by Box2d to out physics.
    private Body body;
    //Current sprite to be drawn in this iteration of the world step.
    private Sprite currentSprite;
    private Sprite[][] animation;
    private PlayerDirection direction = PlayerDirection.FRONT;
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);
    private Vector2 dimensions = new Vector2(0,0);

    public Janitor(float xPos, float yPos, GameScreen game) {
        this.game = game;
        world = game.getWorld();
        position.x = xPos;
        position.y = yPos;
        initSpriteArray();
        currentSprite = animation[0][0];
        dimensions.x = currentSprite.getWidth();
        dimensions.y = currentSprite.getHeight();
        createPhysicsBody();
    }

    public void moveJanitor() {
        if(input.isKeyPressed(Input.Keys.UP) ||input.isKeyPressed(Input.Keys.W)) {
            direction = PlayerDirection.BACK;
            setVelocity();
            currentSprite = animation[2][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.DOWN) || input.isKeyPressed(Input.Keys.S)) {
            direction = PlayerDirection.FRONT;
            setVelocity();
            currentSprite = animation[0][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = PlayerDirection.RIGHT;
            setVelocity();
            currentSprite = animation[1][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.LEFT)) {
            direction = PlayerDirection.LEFT;
            setVelocity();
            currentSprite = animation[3][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        body.setLinearVelocity(velocity);
    }

    private void setVelocity() {
        if(!game.mapCollisionWillOccur()) {
            moveIterCounter++;
            switch(direction) {
                case FRONT:
                    velocity.y -= SPEED;
                    break;
                case RIGHT:
                    velocity.x += SPEED;
                    break;
                case BACK:
                    velocity.y += SPEED;
                    break;
                case LEFT:
                    velocity.x -= SPEED;
                    break;
            }
        }
    }

    public void updateTimers() {
        remainingInvulnerability -= Gdx.graphics.getDeltaTime();
    }

    //Converts from Box2d coordinate system to LibGdx coordinate system and stores them in the position vector.
    public void updateJanitorPosition() {
        position.x = Constants.PIXELS_TO_METERS * body.getPosition().x - currentSprite.getWidth()/2;
        position.y = Constants.PIXELS_TO_METERS * body.getPosition().y - currentSprite.getHeight()/2;
        updateSpritePositions();
    }

    private void updateSpritePositions() {
        for (Sprite[] directionalSpriteSet: animation) {
            for(Sprite s: directionalSpriteSet) {
                s.setX(position.x);
                s.setY(position.y);
            }
        }
    }

    public void resetVelocity() {
        velocity.x = 0;
        velocity.y = 0;
    }

    //Draws the current sprite and flashes during invulnerability.
    public void draw(SpriteBatch batch) {
        if(remainingInvulnerability > 0 && System.currentTimeMillis() % 400 < 150) {
            return;
        }
        currentSprite.draw(batch);
    }

    private void initSpriteArray() {
        animation = new Sprite[4][4];
        Texture texture = new Texture(Gdx.files.internal("Sprites/Player/Stand.png"));
        int width = texture.getWidth();
        int height = texture.getHeight();
        //Forward Sprite Set
        animation[0][0] = new Sprite(texture,width,height);
        animation[0][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Right Step.png")),width,height);
        animation[0][2] = new Sprite(texture,width,height);
        animation[0][3] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Left Step.png")),width,height);
        //Right Sprite Set
        texture = new Texture(Gdx.files.internal("Sprites/Player/Right Side Stand.png"));
        animation[1][0] = new Sprite(texture,width,height);
        animation[1][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Right Side Right Step.png")),width,height);
        animation[1][2] = new Sprite(texture,width,height);
        animation[1][3] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Right Side Left Step.png")),width,height);
        //Back Sprite Set
        texture = new Texture(Gdx.files.internal("Sprites/Player/Back Stand.png"));
        animation[2][0] = new Sprite(texture,width,height);
        animation[2][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Back Right Step.png")),width,height);
        animation[2][2] = new Sprite(texture,width,height);
        animation[2][3] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Back Left Step.png")),width,height);
        //Left Sprite Set
        texture = new Texture(Gdx.files.internal("Sprites/Player/Left Side Stand.png"));
        animation[3][0] = new Sprite(texture,width,height);
        animation[3][1] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Left Side Right Step.png")),width,height);
        animation[3][2] = new Sprite(texture,width,height);
        animation[3][3] = new Sprite(new Texture(Gdx.files.internal("Sprites/Player/Left Side Left Step.png")),width,height);
        updateSpritePositions();
    }

    private void createPhysicsBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((currentSprite.getX() + (currentSprite.getWidth()/2)) / Constants.PIXELS_TO_METERS,
                (currentSprite.getY() + currentSprite.getHeight()/2) / Constants.PIXELS_TO_METERS);
        this.body = this.world.createBody(bodyDef);
    }

    public PlayerDirection getDirection() {
        return direction;
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void takeDamage(LifeBar health) {
        if(remainingInvulnerability <= 0) {
            health.takeDamage();
            remainingInvulnerability = 1;
        }
    }

}