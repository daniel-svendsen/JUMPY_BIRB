package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import se.yrgo.jumpybirb.utils.InputHandler;

public class Birb {
    public static final String TAG = Birb.class.getSimpleName();
    private static final int GRAVITY = -900;
    private static final float INITIAL_POSITION_X = 50;
    private static final float INITIAL_POSITION_Y = 500;
    private static final float[] INITIAL_POSITION = {INITIAL_POSITION_X, INITIAL_POSITION_X};
    private static final float JUMP_VELOCITY = 400f;
    private static final float MOVEMENT_SPEED = 250f;
    private final int width;
    private final int height;
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private Sound flapSound;
    private Circle bounds;

    private TextureRegion[] animationFrames;
    private Animation<TextureRegion> animation;
    private float stateTime;

    public Birb(int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        velocity = new Vector2(0, 0);
        texture = new Texture("Birb1.png");
        loadAnimationFrames();
        // Calculate the radius for the bounds
        float radius = Math.min(width, height / 3); // 33% of the current size

        // Calculate the position offset to keep the birb in the middle
        float xOffset = (width - radius) / 1.2f;
        float yOffset = (height - radius) / 1.2f;

        bounds = new Circle(position.x + xOffset, position.y + yOffset, radius);
        flapSound = Gdx.audio.newSound(Gdx.files.internal("wing.ogg"));
    }

    private void loadAnimationFrames() {
        // Load three textures for animation frames
        TextureRegion frame1 = new TextureRegion(new Texture("Birb2.png"));
        TextureRegion frame2 = new TextureRegion(new Texture("Birb3.png"));
        TextureRegion frame3 = new TextureRegion(new Texture("Birb4.png"));

        // Add frames to animation frames array
        animationFrames = new TextureRegion[]{frame1, frame2, frame3};

        // Create animation
        animation = new Animation<TextureRegion>(0.2f, animationFrames);
        stateTime = 0f;
    }

    public void update(float deltaTime) {
        // Apply gravity to the birb
        velocity.y += GRAVITY * deltaTime;

        // Update birb's position based on velocity
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Adjust MOVEMENT_SPEED to control the birb's horizontal movement
        position.x += MOVEMENT_SPEED * deltaTime;

        ensureInBounds();

        // Update the position of bounds with the new offset
        float radius = Math.min(width, height / 3); // 33% of the current size
        float xOffset = (width - radius) / 1.2f;
        float yOffset = (height - radius) / 1.2f;
        bounds.setPosition(position.x + xOffset, position.y + yOffset);

        // Update animation state
        stateTime += deltaTime;
        animate();
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

    //TODO remove this method after debugging
    public void renderBounds(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 0.5f));        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(bounds.x, bounds.y, bounds.radius);
        shapeRenderer.end();
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
        // Call animation method
        animate();
    }
    private void animate() {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        texture = currentFrame.getTexture();
    }
    public Circle getBounds() {
        return bounds;
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
