package com.nightshift.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by andre on 3/7/2017.
 */
public class GhostSandbox {
    private final float SPEED = 100;
    private final float RANGE = 150;
    private final int ANIMATION_FACTOR = 4;
    private int moveIterCounter = 0;
    private boolean onPatrol = true;
    private Sprite[] animation;
    private Body body;
    private World world;
    private Vector2 position = new Vector2(0,0);
    private Janitor hero;

    public Sprite currentSprite;
    public Vector2 velocity = new Vector2(0,0);

    public GhostSandbox(Janitor hero, int xPos, int yPos, World world) {
        this.world = world;
        this.hero = hero;
        position.x = xPos;
        position.y = yPos;
        initSpriteArray();
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
        body.setLinearVelocity(velocity);
    }

    private void chase() {
        velocity.x = position.x - hero.getX();
        velocity.y = position.y - hero.getY();
    }

    private void patrol() {}

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
        velocity.x = 0;
        velocity.y = 0;
    }

    public void draw(SpriteBatch batch) {
        currentSprite.draw(batch);
    }
}
