package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ground {
    public static final String TAG = Birb.class.getSimpleName();
    private Texture groundTexture;
    private Vector2 position;
    private Rectangle bounds;
    private float speed;

    public Ground(float x, float y, float speed) {
        this.groundTexture = new Texture("Ground2.png");
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x, y, groundTexture.getWidth(), groundTexture.getHeight());
    }

    /**
     * Reset ground position when it goes off-screen
     * and move ground with obstacles
     *
     * @param delta
     */
    public void update(float delta) {
        position.x -= speed * delta;
        bounds.setX(position.x); // Update bounds position
        if (position.x < -groundTexture.getWidth()) {
            position.x += groundTexture.getWidth();
            bounds.setX(position.x); // Update bounds position after reset
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float currentX = getPosition().x;
        while (currentX < camera.position.x + camera.viewportWidth / 2f) {
            batch.draw(groundTexture, currentX, 0, groundTexture.getWidth(), groundTexture.getHeight());
            currentX += groundTexture.getWidth();
        }
    }

    public void dispose() {
        groundTexture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
