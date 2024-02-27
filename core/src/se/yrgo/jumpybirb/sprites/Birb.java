package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Birb {
    private static final int GRAVITY = -800;
    private static final int INITIAL_POSITION_X = 50;
    private static final int INITIAL_POSITION_Y = 500;
    private static final int JUMP_VELOCITY = 300;
    private static final float MOVEMENT_SPEED = 80;

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
        // Apply gravity to the birb
        velocity.y += GRAVITY * deltaTime;

        // Update birb's position based on velocity
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Adjust MOVEMENT_SPEED to control the birb's horizontal movement
        position.x += MOVEMENT_SPEED * deltaTime;

        // Ensure birb stays within the bounds of the screen
        if (position.y < 0)
            position.y = 0;
        if (position.y + texture.getHeight() > 800)
            position.y = 800 - texture.getHeight();
        if (position.x < 0)
            position.x = 0;  // Adjust this condition based on your game's requirements
        // Optionally, handle collisions or other game mechanics here

        bounds.setPosition(position.x, position.y);
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