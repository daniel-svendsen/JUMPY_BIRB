package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Birb2 {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private boolean isJumping;
    private float jumpVelocity;
    private float gravity;
    private Rectangle bounds;
    private Sound flap;

    public Birb2() {
        texture = new Texture("ugly-bird.png");
        position = new Vector2(50, 50); // starting position
        velocity = new Vector2();
        speed = 100; // adjust the speed as needed
        isJumping = false;
        jumpVelocity = 250; // Adjust the jump velocity as needed
        gravity = 500; // Adjust gravity as needed
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("wing.ogg"));
    }

    public void update(float delta) {
        // Movement
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocity.y = speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocity.y = -speed;
        } else {
            velocity.y = 0;
        }

        // Jumping logic
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            velocity.y = jumpVelocity;
        }

        // Applying gravity
        if (isJumping) {
            velocity.y -= gravity * delta;
        }

        // Update position
        position.add(velocity.x * delta, velocity.y * delta);

        // Check for landing
        if (position.y <= 0) {
            position.y = 0;
            isJumping = false;
        }

        // Update bounds
        bounds.setPosition(position);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}