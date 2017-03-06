package com.nightshift.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by andre on 3/6/2017.
 */
public class JanitorSandbox {
    private Sprite[] animation;
    private Body body;
    private World world;
    private Vector2 position;

    public Sprite currentSprite;
    public Vector2 velocity = new Vector2(0,0);

    public JanitorSandbox(int xPos, int yPos, World world) {
        position.x = xPos;
        position.y = yPos;
        this.world = world;
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

    private void updateSpritePositions() {
        for (Sprite s: animation) {
            s.setX(position.x);
            s.setY(position.y);
        }
    }

    public Body getBody() {
        return this.body;
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
}
