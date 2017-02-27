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
import java.util.Random;

/**
 * Created by Cui on 2/22/2017.
 * Most methods come from Janitor class. I didn't do inheritance here because we'll potentially make this class very different.
 */
public class Ghost extends Sprite {
    public static final int STEP_SIZE = 50;

    private static Texture img = new Texture(Gdx.files.internal("Sprite.png"));
    private Body body;
    private World world;
    private float prevX, prevY;

    public Ghost(int xPos, int yPos, World world) {
        super(img,img.getWidth(), img.getHeight());
        this.setX(xPos);
        this.setY(yPos);
        this.world = world;
        prevX = xPos;
        prevY = yPos;
        createPhysicsBody();
    }

    public void wander() {
        for (int i = 0; i < 100; i ++){
            Random rand = new Random();
            int angle = rand.nextInt(360) + 1;
            float xPos = (float) ((STEP_SIZE * Math.sin(angle)));
            float yPos = (float) ((STEP_SIZE) * Math.cos(angle));
            this.translateX(xPos);
            this.translateY(yPos);
            this.body.setTransform(this.getX(),this.getY(),angle);
        }

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
