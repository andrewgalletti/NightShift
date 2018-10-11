package com.nightshift.game.sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Ghost {
    private static final float RANGE = 225;
    private static final float BASE_ALPHA = .3f;
    private static final int ANIMATION_FACTOR = 6;
    private static Random random = new Random();

    private float speed = random.nextFloat() * 20 + 50;
    private int moveIterCounter = 0;
    private boolean onPatrol = true;

    private World world;
    private Janitor hero;
    private Body body;
    private Sprite currentSprite;
    private float spriteScaleFactor;
    private Sprite[] animation;
    private int p;

    private Vector2 position = new Vector2(0,0);
    private Vector2 velocity = new Vector2(0,0);
    //Acceleration manipulation used to give ghost movement curvature.
    private Vector2 acceleration = new Vector2(0,0);
    private Vector2 post = new Vector2(0,0);

    public Ghost(Janitor hero, float xPos, float yPos, World world, float spriteScaleFactor, float speed) {
        this.speed = speed;
        this.world = world;
        this.hero = hero;
        this.spriteScaleFactor = spriteScaleFactor;
        position.x = xPos;
        position.y = yPos;
        post.x = xPos;
        post.y = yPos;
        initSpriteArray();
        currentSprite = animation[0];
        createPhysicsBody();
    }

    private void playSound() {
        /**
         * What this function actually does is checking if hero is out of distance so that
         * ghost should go on patrol, i.e. wander randomly, instead of chase the character.
         */
        boolean prevOnPatrol = onPatrol;
        onPatrol = Math.sqrt(Math.pow(position.x-hero.getX(),2)+Math.pow(position.y-hero.getY(),2)) > RANGE*spriteScaleFactor;
    }

    public void moveGhost() {
        /**
         * Moves ghost:
         * If character is within distance, ghost chase him.
         * Otherwise ghost wanders randomly.
         */
        playSound();
        if(onPatrol) {
            patrol();
            p = 0;
        }
        else {
            chase();
            p = 6;
        }
        applyAlpha();
        moveIterCounter++;
        currentSprite = animation[(moveIterCounter/ANIMATION_FACTOR%6)+p];

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
            if(moveIterCounter % 90 == 0) {
                //Length of the velocity vector.
                double magnitudeOfVelocity = Math.sqrt(Math.pow(velocity.x,2)+Math.pow(velocity.y,2));
                //Standard position angle of velocity, in radians, calculated with inverse cosine to avoid undefined solutions.
                double angleOfVelocity = Math.acos(Math.abs(velocity.x)/magnitudeOfVelocity);
                //Randomly assigned positive value of acceleration.
                double magnitudeOfAcceleration = 5*random.nextDouble();
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
        Texture t0 = new Texture(Gdx.files.internal("Sprites/Ghost/Ghost.png"));
        Texture t1 = new Texture(Gdx.files.internal("Sprites/Ghost/Ghost2.png"));
        Texture t2 = new Texture(Gdx.files.internal("Sprites/Ghost/Ghost3.png"));
        Texture t3 = new Texture(Gdx.files.internal("Sprites/Ghost/Ghost4.png"));

        Texture m0 = new Texture(Gdx.files.internal("Sprites/Ghost/Mad Ghost1.png"));
        Texture m1 = new Texture(Gdx.files.internal("Sprites/Ghost/Mad Ghost2.png"));
        Texture m2 = new Texture(Gdx.files.internal("Sprites/Ghost/Mad Ghost3.png"));
        Texture m3 = new Texture(Gdx.files.internal("Sprites/Ghost/Mad Ghost4.png"));

        animation = new Sprite[12];
        animation[0] = new Sprite(t0,t0.getWidth(),t0.getHeight());
        animation[1] = new Sprite(t1,t1.getWidth(),t1.getHeight());
        animation[2] = new Sprite(t2,t2.getWidth(),t2.getHeight());
        animation[3] = new Sprite(t3,t3.getWidth(),t3.getHeight());
        animation[4] = new Sprite(t2,t2.getWidth(),t2.getHeight());
        animation[5] = new Sprite(t1,t1.getWidth(),t1.getHeight());

        animation[6] = new Sprite(m0,m0.getWidth(),m0.getHeight());
        animation[7] = new Sprite(m1,m1.getWidth(),m1.getHeight());
        animation[8] = new Sprite(m2,m2.getWidth(),m2.getHeight());
        animation[9] = new Sprite(m3,m3.getWidth(),m3.getHeight());
        animation[10] = new Sprite(m2,m2.getWidth(),m2.getHeight());
        animation[11] = new Sprite(m1,m1.getWidth(),m1.getHeight());


        for(Sprite s: animation) {
            s.setAlpha(BASE_ALPHA);
        }
        setSpriteScale(spriteScaleFactor);
        updateSpritePositions();
    }

    public void updateGhostPosition() {
        /**
         *Updates ghost's physical position as opposed to sprite position.
         */
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

    public float getWidth() {
        return currentSprite.getWidth();
    }

    public float getHeight() {
        return currentSprite.getHeight();
    }

    public void draw(SpriteBatch batch) {
        currentSprite.draw(batch);
    }

    private void createPhysicsBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(currentSprite.getX(), currentSprite.getY());
        this.body = this.world.createBody(bodyDef);
    }

    private void setSpriteScale(float scaleFactor){
        for(int i = 0; i < 12; i++){
            animation[i].setScale(scaleFactor);
        }
    }

    private void applyAlpha() {
        if(onPatrol) {
            for (Sprite s: animation) {
                s.setAlpha(BASE_ALPHA);
            }
        }
        else {
            for(Sprite s: animation) {
                float d = (float)(Math.sqrt(Math.pow(position.x-hero.getX(),2)+Math.pow(position.y-hero.getY(),2)));
                float alpha = (1 - BASE_ALPHA) * (RANGE*spriteScaleFactor - d) / RANGE*spriteScaleFactor + BASE_ALPHA;
                s.setAlpha(alpha);
            }
        }
    }
}