package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Birb {
    private static final int GRAVITY = -10;
    private static final int INITIAL_POSITION_X = 50;
    private static final int INITIAL_POSITION_Y = 500;
    private static final int JUMP_VELOCITY = 250;

    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private Sound flapSound;
    private Rectangle bounds;

    public Birb() {
        position = new Vector2(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        velocity = new Vector2(0, 0);
        texture = new Texture("Birb1.png");
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        flapSound = Gdx.audio.newSound(Gdx.files.internal("wing.ogg"));
    }

    public void update(float deltaTime) {
        if (position.y > 0)
            velocity.add(0, GRAVITY);
        velocity.scl(deltaTime);

        position.add(0, velocity.y);

        if (position.y < 0)
            position.y = 0;

        if (position.y + texture.getHeight() > 800)
            position.y = 800 - texture.getHeight();

        bounds.setPosition(position.x, position.y);

        velocity.scl(1 / deltaTime);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void jump() {
        velocity.y = JUMP_VELOCITY;
        flapSound.play(0.3f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
        flapSound.dispose();
    }
}