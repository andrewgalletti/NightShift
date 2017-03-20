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
    private final int ATTACK_RANGE = 70;
    private final int ANIMATION_FACTOR = 4;

    private int lives = Integer.MAX_VALUE;
    private int moveIterCounter = 0;
    private float remainingInvulnerability;

    private World world;
    private Body body;
    private Sprite currentSprite;
    private Sprite[][] animation;
    private PlayerDirection direction = PlayerDirection.FRONT;
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);

    public Janitor(int xPos, int yPos, World world) {
        position.x = xPos;
        position.y = yPos;
        this.world = world;
        initSpriteArray();
        currentSprite = animation[0][0];
        createPhysicsBody();
    }

    public void moveJanitor() {
        boolean didMove = false;
        if(input.isKeyPressed(Input.Keys.UP)) {
            velocity.y += SPEED;
            direction = PlayerDirection.BACK;
            didMove = true;
        }
        if(input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y -= SPEED;
            direction = PlayerDirection.FRONT;
            didMove = true;
        }
        if(input.isKeyPressed(Input.Keys.RIGHT)) {
            velocity.x += SPEED;
            direction = PlayerDirection.RIGHT;
            didMove = true;
        }
        if(input.isKeyPressed(Input.Keys.LEFT)) {
            velocity.x -= SPEED;
            direction = PlayerDirection.LEFT;
            didMove = true;
        }

        if(didMove) {
            moveIterCounter++;
            switch(direction) {
                case FRONT:
                    currentSprite = animation[0][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
                case RIGHT:
                    currentSprite = animation[1][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
                case BACK:
                    currentSprite = animation[2][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
                case LEFT:
                    currentSprite = animation[3][moveIterCounter/ANIMATION_FACTOR%animation.length];
                    break;
            }
        }
        body.setLinearVelocity(velocity);
    }

    public void update() {
        remainingInvulnerability -= Gdx.graphics.getDeltaTime();
    }

    public void updateJanitorPosition() {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
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
        if(remainingInvulnerability > 0 && System.currentTimeMillis() % 400 < 150) {
            return;
        }
        currentSprite.draw(batch);
    }

    private void initSpriteArray() {
        animation = new Sprite[4][4];
        Texture texture = new Texture(Gdx.files.internal("Stand.png"));
        int width = texture.getWidth();
        int height = texture.getHeight();
        //Forward Sprite Set
        animation[0][0] = new Sprite(texture,width,height);
        animation[0][1] = new Sprite(new Texture(Gdx.files.internal("Right Step.png")),width,height);
        animation[0][2] = new Sprite(texture,width,height);
        animation[0][3] = new Sprite(new Texture(Gdx.files.internal("Left Step.png")),width,height);
        //Right Sprite Set
        texture = new Texture(Gdx.files.internal("Right Side Stand.png"));
        animation[1][0] = new Sprite(texture,width,height);
        animation[1][1] = new Sprite(new Texture(Gdx.files.internal("Right Side Right Step.png")),width,height);
        animation[1][2] = new Sprite(texture,width,height);
        animation[1][3] = new Sprite(new Texture(Gdx.files.internal("Right Side Left Step.png")),width,height);
        //Back Sprite Set
        texture = new Texture(Gdx.files.internal("Back Stand.png"));
        animation[2][0] = new Sprite(texture,width,height);
        animation[2][1] = new Sprite(new Texture(Gdx.files.internal("Back Right Step.png")),width,height);
        animation[2][2] = new Sprite(texture,width,height);
        animation[2][3] = new Sprite(new Texture(Gdx.files.internal("Back Left Step.png")),width,height);
        //Left Sprite Set
        texture = new Texture(Gdx.files.internal("Left Side Stand.png"));
        animation[3][0] = new Sprite(texture,width,height);
        animation[3][1] = new Sprite(new Texture(Gdx.files.internal("Left Side Right Step.png")),width,height);
        animation[3][2] = new Sprite(texture,width,height);
        animation[3][3] = new Sprite(new Texture(Gdx.files.internal("Left Side Left Step.png")),width,height);
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

    public void takeDamage() {
        if(remainingInvulnerability < 0) {
            lives--;
            remainingInvulnerability = 4;
            System.out.println("Took damage");
        }
    }

    public boolean isDead() {
        return lives < 1;
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
}