package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.utils.ScoreManager;

/***
 * The screen that runs the actual game, a round of the game.
 */
public class PlayScreen implements Screen {
    private static final String TAG = SplashScreen.class.getName();
    private static final Float TEXT_FONT_SCALE = 2.0f;
    BitmapFont textFont;
    private SpriteBatch batch;
    private Texture playerTexture;
    private GameState state;
    private ScoreManager scoreManager;

    /***
     * This is used to tell which state the playScreen is in.
     * Not implemented yet...
     */
    public enum GameState {
        MENU, READY, RUNNING, GAMEOVER
    }

    /**
     * Constructor. Initialize ScoreManager.
     */
    public PlayScreen() {
        this.scoreManager = ScoreManager.getInstance();
        this.state = GameState.READY;
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();

        playerTexture = new Texture("ugly-bird.png"); // Change the birb texture here later :)

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textFont.getData().setScale(TEXT_FONT_SCALE);

    }

    /***
     * This method is called when the Application should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.35f, 0.4f, 1); // Set the background color

        // Clear the frame before rendering anything else.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Some placeholder text
        batch.begin();
        float textPadding = 50f; // Adjust the padding between text elements
        textFont.draw(batch, "Press Esc to go to Menu", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 3f - textPadding, 0, Align.left, false);

        batch.end();

        // Render bird
        batch.begin();
        batch.draw(playerTexture, Gdx.graphics.getWidth() / 3f, 200, 250, 250);
        batch.end();

        // Update score
        scoreManager.update(delta);
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        // Render the scores on the screen
        batch.begin();
        textFont.draw(batch, "Score: " + currentScore, 10, Gdx.graphics.getHeight() - 10f);
        textFont.draw(batch, "High Score: " + highScore, 10, Gdx.graphics.getHeight() - 10f - textPadding);
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
        // could do something here maybe
    }

    /***
     * This method is called when the Application is paused,
     * usually when it's not active or visible on-screen.
     */
    @Override
    public void pause() {
        // could do something here maybe
    }

    /***
     * This method is called when the Application is resumed from
     * a paused state, usually when it regains focus.
     */
    @Override
    public void resume() {
        // could do something here maybe
    }

    /***
     * This method is called when this screen is no longer
     * the current screen for the game.
     */
    @Override
    public void hide() {
        // could do something here maybe
    }

    /***
     * This method is called when this screen should
     * release all resources. Preceded by a call to pause().
     */
    @Override
    public void dispose() {
        // could do something here maybe
    }
}
