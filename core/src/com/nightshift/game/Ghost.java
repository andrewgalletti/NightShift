package com.nightshift.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import java.util.Random;

/**
 * Created by Cui on 2/22/2017.
 * Most methods come from Janitor class. I didn't do inheritance here because we'll potentially make this class very different.
 */
public class Ghost extends Sprite {

    private static final int RANGE = 200;
    private static Texture img = new Texture(Gdx.files.internal("Ghost.png"));
    private Body body;
    private World world;
    private Janitor hero;
    private boolean onPatrol = true;

    public Ghost(Janitor hero, int xPos, int yPos, World world) {
        super(img,img.getWidth(), img.getHeight());
        this.hero = hero;
        this.setX(xPos);
        this.setY(yPos);
        this.world = world;
        createPhysicsBody();
    }

    public void moveGhost() {
        onPatrol = Math.sqrt(Math.pow(this.getX()-hero.getX(),2)+Math.pow(this.getY()-hero.getY(),2)) > RANGE;
        if(onPatrol) {
            patrol();
        }
        else {
            chase();
        }
    }

    private void chase() {
        float targetX = hero.getX(); //Player's position
        float targetY = hero.getY();
        float spriteX = getX(); //Ghost's
        float spriteY = getY();
        float x2 = getX(); //Ghost's new position
        float y2 = getY();
        float angle;
        angle = (float) Math.atan2(targetY - spriteY, targetX - spriteX);
        x2 += (float) Math.cos(angle) * 125 * Gdx.graphics.getDeltaTime();
        y2 += (float) Math.sin(angle) * 125 * Gdx.graphics.getDeltaTime();
        setPosition(x2, y2); //Set enemy's new positions.
    }

    private void patrol() {
        
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

    public Body getBody() {
        return this.body;
    }
}
