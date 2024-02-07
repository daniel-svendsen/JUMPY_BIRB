package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Birb {
    private static final int GRAVITY = -10;
    public static int MOVEMENT = 0;
    private Vector3 position;
    private Vector3 velocity;
    private Texture birb; // Birb Texture
    private Sound flap;
    private Rectangle bounds; // Required for collision

    public Birb(int x, int y) {

        position = new Vector3(50, 50, 0);
        velocity = new Vector3(0, 0, 0);
        birb = new Texture("Birb1.png");
        bounds = new Rectangle(x, y, birb.getWidth(), birb.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("wing.ogg"));
    }

    public void update(float dt) {
        if (position.y > 0)
            velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);

        position.add(MOVEMENT * dt, velocity.y, 0);

        if (position.y < 0)
            position.y = 0;
        velocity.scl(1 / dt);

        // Check if the bird is going out of the screen from the top
        if (position.y + birb.getHeight() > 800)
            position.y = 800 - birb.getHeight();

        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return birb;
    }

    public void jump() {
        velocity.y = 250;
        flap.play(0.3f);
    }

    public Rectangle getBounds() {
        return  bounds;
    }

    public void dispose() {
        birb.dispose();
        flap.dispose();
    }
}
