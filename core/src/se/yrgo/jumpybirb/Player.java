package se.yrgo.jumpybirb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Texture playerTexture;
    private Vector2 position;
    private float speed;
    private boolean isJumping;
    private float jumpVelocity;
    private float gravity = 300; // Adjust gravity as needed

    public Player() {
        playerTexture = new Texture("dragon.png");
        position = new Vector2(50, 50); // starting position
        speed = 50; // adjust the speed as needed
        isJumping = false;
        jumpVelocity = 50; // Adjust the jump velocity as needed
    }

    public void update(float delta) {
        // Jumping logic
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            jumpVelocity = 200; // Reset jump velocity
        }

        // Applying gravity
        if (isJumping) {
            position.y += jumpVelocity * delta;
            jumpVelocity -= gravity * delta;

            // Check if the player has landed
            if (position.y <= 1) {
                position.y = 1;
                isJumping = false;
            }
        }
    }
    // public void update(float delta) {
    // if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isJumping) {
    // isJumping = true;
    // position.y += jumpVelocity * delta;
    // } // Adjust the initial jump position
    // // Update player position based on input or game logic
    // //position.x += speed * delta; // move to the right
    // if (isJumping) {
    // position.y -= jumpVelocity * delta;
    // if (position.y <= 1) { // Adjust the ground level as needed
    // position.y = 1;
    // isJumping = false;
    // }
    // }
    // }

    public void render(SpriteBatch batch) {
        batch.draw(playerTexture, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }
}
// Add more methods as needed for player behavior
