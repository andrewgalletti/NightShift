package com.nightshift.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class LifeBar extends Sprite {

    private NightShift game;
    private int lives = 4;
    private ArrayList<Sprite> hearts;

    public LifeBar(NightShift game) {
        this.game = game;
        Texture texture = new Texture(Gdx.files.internal("Sprites/Heart.png"));
        float scale = .07f;
        float w = scale * texture.getWidth();
        hearts = new ArrayList<Sprite>();

        for(int i = 0; i < lives; i++) {
            Sprite s = new Sprite(texture);
            s.setScale(scale);
            s.setPosition((int)(Gdx.graphics.getWidth()/1.9) - (w * i) - 10, (int)(Gdx.graphics.getHeight()/1.725));
            hearts.add(s);
        }
    }

    public void draw(SpriteBatch batch) {
        for(Sprite heart: hearts) {
            heart.draw(batch);
        }
    }

    public void takeDamage() {
        lives--;
        if(hearts.size() >= 1)
            hearts.remove(lives);
        else
            game.endGame();
    }
}
