package se.yrgo.jumpybirb.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Birb {
    private static final int GRAVITY = -15;
    public static int MOVEMENT = 100;
    private Vector3 position;
    private Vector3 velocity;
    private Texture birb; // Birb Texture
    private Sound flap;
    private Rectangle bounds; // Required for collision

    public Birb(int x, int y) {
        position = new Vector3(x, y, 0); // z axis is 0 because we are not using it
        velocity = new Vector3(0, 0, 0);
        birb = new Texture("dragon.png");
        bounds = new Rectangle(x, y, birb.getWidth(), birb.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
    }

    public void update(float dt) {
        if (position.y > 0)
            velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);

        position.add(MOVEMENT * dt, velocity.y, 0);

        if (position.y < 0)
            position.y = 0;
        velocity.scl(1 / dt);

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
