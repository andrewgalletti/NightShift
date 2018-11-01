package com.nightshift.game.sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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
    //Physical representation of the Janitor used by Box2d to carry out physics.
    private Body body;
    //Current sprite to be drawn in this iteration of the world step.
    private Sprite currentSprite;
    private Sprite[][] animation;
    private float spriteScaleFactor;
    private PlayerDirection direction = PlayerDirection.FRONT;
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);
    private Vector2 dimensions = new Vector2(0,0);

    public Janitor(float xPos, float yPos, GameScreen game, float spriteScaleFactor) {
        this.game = game;
        this.spriteScaleFactor = spriteScaleFactor;
        world = game.getWorld();
        position.x = xPos * (1/spriteScaleFactor);
        position.y = yPos * (1/spriteScaleFactor);
        initSpriteArray();
        dimensions.x = currentSprite.getWidth();//*spriteScaleFactor;
        dimensions.y = currentSprite.getHeight();//*spriteScaleFactor;
        createPhysicsBody();
    }

    public void moveJanitor() {
        /**
         * Sets input direction, and renders character sprite according to direction.
         */
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
        if(input.isKeyPressed(Input.Keys.RIGHT) || input.isKeyPressed(Input.Keys.D)) {
            direction = PlayerDirection.RIGHT;
            setVelocity();
            currentSprite = animation[1][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.LEFT) || input.isKeyPressed(Input.Keys.A)) {
            direction = PlayerDirection.LEFT;
            setVelocity();
            currentSprite = animation[3][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        body.setLinearVelocity(velocity);
    }

    private void setVelocity() {
        /**
         * Move character according to input direction.
         */
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

    public void updateInvulnerabilityTimer() {
        remainingInvulnerability -= Gdx.graphics.getDeltaTime();
    }


    public void updateJanitorPosition() {
        /**
         *  Converts from Box2d coordinate system to LibGdx coordinate system and stores them in the position vector.
         */
        position.x = Constants.PIXELS_TO_METERS * spriteScaleFactor * body.getPosition().x - currentSprite.getWidth()/2;
        position.y = Constants.PIXELS_TO_METERS * spriteScaleFactor * body.getPosition().y - currentSprite.getHeight()/2;
        updateSpritePositions();
    }

    private void updateSpritePositions() {
        for (Sprite[] directionalSpriteSet: animation) {
            for(Sprite s: directionalSpriteSet) {
                float x = position.x;// - (1-spriteScaleFactor)*currentSprite.getWidth()/2;
                float y = position.y;// - (1-spriteScaleFactor)*currentSprite.getWidth()/2;
                s.setX(x);
                s.setY(y);
            }
        }
    }

    public void resetVelocity() {
        velocity.x = 0;
        velocity.y = 0;
    }


    public void draw(SpriteBatch batch) {
        /**
         * Draws the current sprite.
         * After the character gets hit the sprite flashes, and character gets invulnerable for a few seconds.
         */

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

        currentSprite = animation[0][0];
        setSpriteScale(spriteScaleFactor);
        updateSpritePositions();
    }

    private void createPhysicsBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((currentSprite.getX() + (currentSprite.getWidth()/2)) / Constants.PIXELS_TO_METERS,
                (currentSprite.getY() + currentSprite.getHeight()/2) / Constants.PIXELS_TO_METERS);
        this.body = this.world.createBody(bodyDef);
    }

   private void setSpriteScale(float scaleFactor){
       for(int i = 0; i < 4; i++){
           for(int j = 0; j < 4; j++){
               animation[i][j].setScale(scaleFactor);
           }
       }
   }

    public PlayerDirection getDirection() {
        return direction;
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    public Rectangle getWallHitbox(){
        /**
         * Used for wall collision.
         */

        float threshold = 5f*spriteScaleFactor;
        float width = getDimensions().x*0.8f*spriteScaleFactor;
        float height = getDimensions().y*0.8f*spriteScaleFactor;
        float x = getX() + (getDimensions().x/2) - (width/2);
        float y = getY() + (getDimensions().y/2) - (height/2);
        switch(getDirection()) {
            case BACK:
                y += threshold;
                break;
            case LEFT:
                x -= threshold;
                break;
            case FRONT:
                y -= threshold;
                break;
            case RIGHT:
                x += threshold;
                break;
        }
        return new Rectangle( x, y, width, height);
    }

    public Rectangle getGhostHitbox(){
        /**
         * Used for ghost collision.
         */

        float width = getDimensions().x*0.8f*spriteScaleFactor;
        float height = getDimensions().y*0.8f*spriteScaleFactor;
        float x = getX() + (getDimensions().x/2) - (width/2);
        float y = getY() + (getDimensions().y/2) - (height/2);


        return new Rectangle( x, y, width, height);
    }



    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void takeDamage(LifeBar health) {
        /**
         *Drops life, and get invulnerable for a few seconds.
         */
        if(remainingInvulnerability <= 0) {
            health.takeDamage();
            remainingInvulnerability = 3;
        }
    }

}