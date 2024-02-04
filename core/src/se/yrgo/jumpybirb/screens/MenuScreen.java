package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

/***
 * Menu screen that shows after the splash screen when you run the game.
 * Choose to play again, show highscore from current game session, enable/disable music.
 * The game also returns to this screen after the game over screen gets displayed.
 */
public class MenuScreen implements Screen {
    public static final String TAG = MenuScreen.class.getName();
    private static final Float HEADER_FONT_SCALE = 3.0f;
    private static final Float TEXT_FONT_SCALE = 2.0f;

    BitmapFont headerFont;
    BitmapFont textFont;
    SpriteBatch batch;
    private Texture backgroundTexture;


    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();
        headerFont = new BitmapFont();
        headerFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        headerFont.getData().setScale(HEADER_FONT_SCALE);

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        textFont.getData().setScale(TEXT_FONT_SCALE);

        // Load background image
        backgroundTexture = new Texture(Gdx.files.internal("splash.png"));
        Gdx.app.log(TAG, "Image loaded successfully: " + backgroundTexture);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw text "Menu"
        batch.begin();
        headerFont.draw(batch, "Menu",  Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f, 0, Align.left, false);

        // Additional text below "Menu"
        float textPadding = 50f; // Adjust the padding between text elements
        textFont.draw(batch, "Press Enter to play game", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f - textPadding, 0, Align.left, false);

        batch.end();
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
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose() called");
        batch.dispose();
    }
}
