package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

/***
 * The screen that shows when you start up the application.
 */
public class SplashScreen implements Screen {
    public static final String TAG = SplashScreen.class.getName();

    private static final Float FONT_SCALE = 4.0f;
    BitmapFont font;
    SpriteBatch batch;
    private Texture backgroundTexture;
    //private Texture birdTexture;


    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.getData().setScale(FONT_SCALE);

        // Load background image
        backgroundTexture = new Texture(Gdx.files.internal("Welcome1.jpg"));
        Gdx.app.log(TAG, "Image loaded successfully: " + backgroundTexture);

        // Load bird image
        //birdTexture = new Texture(Gdx.files.internal("pigeon.png"));
        //Gdx.app.log(TAG, "Image loaded successfully: " + birdTexture);
    }

    @Override
    public void render(float delta) {
        // Clear the screen before rendering anything
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render background image
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Render bird image
        //batch.begin();
        //batch.draw(birdTexture,  Gdx.graphics.getWidth() / 3f, 200, 250, 250);
        //batch.end();

        // Render text
//        batch.begin();
//        font.setColor(Color.WHITE);
//        font.draw(batch, "Flappy Birb",  Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() * (2f / 3f),
//                0, Align.left, false);
//        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "resize(" + width + ", " + height + ") called");
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause() called");
    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume() called");
    }

    @Override
    public void hide() {
        Gdx.app.log(TAG, "hide() called");
        batch.dispose();
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose() called");
    }
}
