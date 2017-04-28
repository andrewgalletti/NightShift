package com.nightshift.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class LifeBar extends Sprite {

    private NightShift game;
    private int lives = 4;
    private ArrayList<Sprite> hearts;
    private Sound takeDamage;

    public LifeBar(NightShift game) {
        this.game = game;
        takeDamage = Gdx.audio.newSound(Gdx.files.internal("Sounds/TakeDamage.mp3"));

        Texture texture = new Texture(Gdx.files.internal("Sprites/Heart.png"));
        float scale = .07f;
        float w = scale * texture.getWidth();
        hearts = new ArrayList<Sprite>();

        for(int i = 0; i < lives; i++) {
            Sprite s = new Sprite(texture);
            s.setScale(scale);
            s.setPosition((int)(Constants.VIEWPORT_WIDTH / 1.88 - (i * w) - 10),(int)(Constants.VIEWPORT_HEIGHT / 1.72));
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
        takeDamage.play();
        if(hearts.size() > 1)
            hearts.remove(lives);
        else
            game.endGame();
    }
}
