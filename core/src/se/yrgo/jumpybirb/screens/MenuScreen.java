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
 * The menu screen that always shows after the splash screen when you run the game.
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

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("Welcome1.jpg"));
        headerFont = new BitmapFont();
        headerFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        headerFont.getData().setScale(HEADER_FONT_SCALE);

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        textFont.getData().setScale(TEXT_FONT_SCALE);


    }

    /***
     * This method is called when the Application should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw text "Menu"
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        headerFont.draw(batch, "Menu",  Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f, 0, Align.left, false);

        // Additional text below "Menu"
        float textPadding = 50f; // Adjust the padding between text elements
        textFont.draw(batch, "Press Enter to play game", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f - textPadding, 0, Align.left, false);

        batch.end();
    }

    /***
     * This method is called when the Application is resized,
     * which can happen at any point during a non-paused state.
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "resize(" + width + ", " + height + ") called");
    }

    /***
     * This method is called when the Application is paused,
     * usually when it's not active or visible on-screen.
     */
    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause() called");
    }

    /***
     * This method is called when the Application is resumed from
     * a paused state, usually when it regains focus.
     */
    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume() called");
    }

    /***
     * This method is called when this screen is no longer
     * the current screen for the game.
     */
    @Override
    public void hide() {
        Gdx.app.log(TAG, "hide() called");
    }

    /***
     * This method is called when this screen should
     * release all resources. Preceded by a call to pause().
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose() called");
        batch.dispose();
    }
}
