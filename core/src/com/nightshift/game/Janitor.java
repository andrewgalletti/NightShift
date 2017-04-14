package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import static com.badlogic.gdx.Gdx.input;

public class Janitor {
    private final float SPEED = 2f;
    private final int ATTACK_RANGE = 70;
    private final int ANIMATION_FACTOR = 4;

    public int lives = 40;
    private int moveIterCounter = 0;
    private float remainingInvulnerability;
    private float remainingAttackDelay;

    private GameScreen game;
    private World world;
    private Body body;
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
        if(input.isKeyPressed(Input.Keys.UP)) {
            direction = PlayerDirection.BACK;
            movementHelper();
            currentSprite = animation[2][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.DOWN)) {
            direction = PlayerDirection.FRONT;
            movementHelper();
            currentSprite = animation[0][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = PlayerDirection.RIGHT;
            movementHelper();
            currentSprite = animation[1][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        if(input.isKeyPressed(Input.Keys.LEFT)) {
            direction = PlayerDirection.LEFT;
            movementHelper();
            currentSprite = animation[3][moveIterCounter/ANIMATION_FACTOR%animation.length];
        }
        body.setLinearVelocity(velocity);
    }

    private void movementHelper() {
        if(!game.mapCollisionWillOccur()) {
            moveIterCounter++;
            switch(direction) {
                case FRONT:
                    velocity.y -= SPEED;
                    //currentSprite = animation[0][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
                case RIGHT:
                    velocity.x += SPEED;
                    //currentSprite = animation[1][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
                case BACK:
                    velocity.y += SPEED;
                    //currentSprite = animation[2][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
                case LEFT:
                    velocity.x -= SPEED;
                    //currentSprite = animation[3][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
            }
        }
        else {
            System.out.println("No go bro.\n" + velocity.x + ", " + velocity.y);
        }

    }

    public void updateTimers() {
        remainingInvulnerability -= Gdx.graphics.getDeltaTime();
        remainingAttackDelay -= Gdx.graphics.getDeltaTime();
    }

    public void updateJanitorPosition() {
        position.x = Constants.PIXELS_TO_METERS * body.getPosition().x - currentSprite.getWidth()/2;
        position.y = Constants.PIXELS_TO_METERS * body.getPosition().y - currentSprite.getHeight()/2;
        updateSpritePositions();
    }

    private void updateSpritePositions() {
        for (Sprite[] directionalSprites: animation) {
            for(Sprite s: directionalSprites) {
                s.setX(position.x);
                s.setY(position.y);
            }
        }
    }

    public void revertPosition() {
        System.out.println("REVERTED");
        body.setTransform((position.x + currentSprite.getWidth() / 2) / Constants.PIXELS_TO_METERS,
                (position.y + currentSprite.getHeight() / 2) / Constants.PIXELS_TO_METERS, body.getAngle());
        updateSpritePositions();
    }

    public void resetVelocity() {
        velocity.x = 0;
        velocity.y = 0;
    }

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
        bodyDef.position.set((currentSprite.getX() + currentSprite.getWidth()/2) / Constants.PIXELS_TO_METERS,
                (currentSprite.getY() + currentSprite.getHeight()/2) / Constants.PIXELS_TO_METERS);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(currentSprite.getWidth() / 2 / Constants.PIXELS_TO_METERS,
                currentSprite.getHeight() / 2 / Constants.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = .5f;
        fixtureDef.density = .1f;

        this.body = this.world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
    }

    public PlayerDirection getDirection() {
        return direction;
    }

    public Body getBody() {
        return this.body;
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

    public void takeDamage() {
        if(remainingInvulnerability < 0) {
            lives--;
            remainingInvulnerability = 4;
            System.out.println("Took damage");
        }
    }

    public boolean isDead() {
        return lives == 0;
    }

    public boolean isGhostInHitBox(Ghost g) {
        double theta = 0;
        boolean withinRange = Math.sqrt(Math.pow(this.getX()-g.getX(),2)+Math.pow(this.getY()-g.getY(),2)) < ATTACK_RANGE;
        if(!withinRange)
            return false;
        switch(direction) {
            case BACK:
                theta = Math.atan(Math.abs(g.getX()-this.getX())/(g.getY()-this.getY()));
                break;
            case RIGHT:
                theta = Math.atan(Math.abs(g.getY()-this.getY())/(g.getX()-this.getX()));
                break;
            case FRONT:
                theta = Math.atan(Math.abs(g.getX()-this.getX())/(this.getY()-g.getY()));
                break;
            case LEFT:
                theta = Math.atan(Math.abs(g.getY()-this.getY())/(this.getX()-g.getX()));
        }
        return theta > 0 && theta <= Math.toRadians(45);
    }

    public void applyAttackDelay() {
        remainingAttackDelay = .5f;
    }

    public boolean readyToAttack() {
        return remainingAttackDelay <= 0 && remainingInvulnerability <= 0;
    }

    private void getPositionData() {
        System.out.println("===Positional Data===\nPosition Vector: (" + position.x + ", " + position.y + ")");
        System.out.println("Body Position: (" + body.getPosition().x + ", " + body.getPosition().y + ")");
        System.out.println("Sprite Position: (" + currentSprite.getX() + ", " + currentSprite.getY() + ")");
    }
}