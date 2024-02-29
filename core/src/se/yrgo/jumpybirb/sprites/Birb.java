package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import se.yrgo.jumpybirb.utils.InputHandler;

public class Birb {
    public static final String TAG = Birb.class.getSimpleName();
    private static final int GRAVITY = -800;
    private static final float INITIAL_POSITION_X = 50;
    private static final float INITIAL_POSITION_Y = 500;
    private static final float[] INITIAL_POSITION = {INITIAL_POSITION_X, INITIAL_POSITION_X};
    private static final float JUMP_VELOCITY = 300f;
    private static final float MOVEMENT_SPEED = 190f;
    private int width;
    private int height;
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private Sound flapSound;
    private Rectangle bounds;

    public Birb(int width, int height) {
        this.width = width;
        this.height = height;
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

        ensureInBounds();

        bounds.setPosition(position.x, position.y);
    }

    private void ensureInBounds() {
        // Ensure birb stays within the bounds of the screen
        if (position.y < 0)
            position.y = 0;
        if (position.y + texture.getHeight() > 800)
            position.y = 800 - (float) texture.getHeight();
        if (position.x < 0)
            position.x = 0;
    }

    public float[] getInitialPosition() {
        return INITIAL_POSITION;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void jump() {
        velocity.y = JUMP_VELOCITY;

        velocity.x = 0; // Add this line to reset horizontal velocity
        flapSound.play(0.3f);
        Gdx.app.log(TAG, "birbJump called inside Birb class, " +
                "velocity.y = " + velocity.y + " ," +
                "velocity.x = " + velocity.x +
                ", position.x: " + position.x + ", position.y: " + position.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void reset() {
        // Reset position and velocity
        position.set(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        velocity.set(0, 0);

        // Reset bounds using updated position
        bounds.setPosition(position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
        flapSound.dispose();
    }
}
