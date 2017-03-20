package com.nightshift.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.Random;

public class Ghost {
    private static final float RANGE = 150;
    private static final int ANIMATION_FACTOR = 4;
    private static Random random = new Random();

    public int lives = 1;
    public int damage = 1;
    private float speed = random.nextFloat() * 10 + 20;
    private int moveIterCounter = 0;
    private boolean onPatrol = true;

    private World world;
    private Janitor hero;
    private Body body;
    private Sprite currentSprite;
    private Sprite[] animation;
    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);
    private Vector2 acceleration = new Vector2(0,0);
    private Vector2 post = new Vector2(0,0);

    public Ghost(Janitor hero, int xPos, int yPos, World world) {
        this.world = world;
        this.hero = hero;
        position.x = xPos;
        position.y = yPos;
        post.x = xPos;
        post.y = yPos;
        initSpriteArray();
        currentSprite = animation[0];
        createPhysicsBody();
    }

    public void moveGhost() {
        onPatrol = Math.sqrt(Math.pow(position.x-hero.getX(),2)+Math.pow(position.y-hero.getY(),2)) > RANGE;
        if(onPatrol) {
            patrol();
        }
        else {
            chase();
        }
        moveIterCounter++;
        currentSprite = animation[moveIterCounter/ANIMATION_FACTOR%animation.length];

        if(velocity.x != 0 && velocity.y != 0)
            scaleVelocity();
        body.setLinearVelocity(velocity);
    }

    private void chase() {
        velocity.x = hero.getX() - position.x;
        velocity.y = hero.getY() - position.y;
    }

    private void patrol() {
        //If outside range of "post" forgoes acceleration manipulation and b-lines it back within range.
        if(Math.sqrt(Math.pow(position.x-post.x,2)+Math.pow(position.y-post.y,2)) > 150) {
            velocity.x += 50*(post.x - position.x);
            velocity.y += 50*(post.y - position.y);
        }
        else {
            if(moveIterCounter % 60 == 0) {
                //Length of the velocity vector.
                double magnitudeOfVelocity = Math.sqrt(Math.pow(velocity.x,2)+Math.pow(velocity.y,2));
                //Standard position angle of velocity, in radians, calculated with inverse cosine to avoid undefined solutions.
                double angleOfVelocity = Math.acos(Math.abs(velocity.x)/magnitudeOfVelocity);
                //Randomly assigned positive value of acceleration.
                double magnitudeOfAcceleration = 3*random.nextDouble();
                //Randomly assigned value of angle, in radians, formed between the velocity and acceleration vectors.
                double angleOfAcceleration = Math.PI/2*random.nextDouble()-Math.PI/4;

                //Converts standard position angle to raw angle, and assigns random angle if directional components of velocity are both zero.
                if(velocity.x < 0 && velocity.y > 0) {
                    angleOfVelocity = Math.PI - angleOfVelocity;
                }
                else if(velocity.x < 0 && velocity.y < 0) {
                    angleOfVelocity = Math.PI + angleOfVelocity;
                }
                else if(velocity.x > 0 && velocity.y < 0) {
                    angleOfVelocity = 2*Math.PI - angleOfVelocity;
                }
                else {
                    if(velocity.x == 0) {
                        if(velocity.y > 0)
                            angleOfVelocity = Math.PI/2;
                        if(velocity.y < 0)
                            angleOfVelocity = 3*Math.PI/2;
                    }
                    if(velocity.y == 0) {
                        if(velocity.x > 0)
                            angleOfVelocity = 0;
                        if(velocity.x < 0)
                            angleOfVelocity = Math.PI;
                    }
                    if(velocity.x == 0 && velocity.y == 0) {
                        angleOfVelocity = 2*Math.PI*random.nextDouble();
                    }
                }

                //Calculates and assigns component vectors of acceleration.
                acceleration.x = (float)(magnitudeOfAcceleration*Math.sin(angleOfAcceleration+angleOfVelocity));
                acceleration.y = (float)(magnitudeOfAcceleration*Math.cos(angleOfAcceleration+angleOfVelocity));
            }

            //Applies acceleration.
            velocity.x += acceleration.x;
            velocity.y += acceleration.y;
        }
    }

    private void initSpriteArray() {
        Texture t0 = new Texture(Gdx.files.internal("Ghost.png"));
        animation = new Sprite[1];
        animation[0] = new Sprite(t0,t0.getWidth(),t0.getHeight());
        updateSpritePositions();
    }

    public void updateGhostPosition() {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        updateSpritePositions();
    }

    private void updateSpritePositions() {
        for(Sprite s: animation) {
            s.setX(position.x);
            s.setY(position.y);
        }
    }

    public void resetVelocity() {
        if(!onPatrol) {
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    private void scaleVelocity() {
        double theta = Math.atan(Math.abs(velocity.x)/Math.abs(velocity.y));
        if(velocity.x < 0) {
            velocity.x = -1*(float)(speed * Math.sin(theta));
        }
        else {
            velocity.x = (float)(speed * Math.sin(theta));
        }
        if(velocity.y < 0) {
            velocity.y = -1*(float)(speed * Math.cos(theta));
        }
        else {
            velocity.y = (float)(speed * Math.cos(theta));
        }
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

    public void mergeGhosts(Ghost g) {
        double totalArea = currentSprite.getWidth() * currentSprite.getHeight() + g.currentSprite.getWidth() * g.currentSprite.getHeight();
        double scaleFactor = Math.sqrt(totalArea/(currentSprite.getWidth()*currentSprite.getHeight()));
        for(Sprite s: animation) {
            s.setScale((float)scaleFactor);
        }
        lives += g.lives;
        damage += g.damage;
    }
}