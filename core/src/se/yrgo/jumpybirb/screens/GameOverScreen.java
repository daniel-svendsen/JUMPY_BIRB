package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.sprites.Birb;
import se.yrgo.jumpybirb.utils.ScoreManager;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;
import se.yrgo.jumpybirb.utils.Screens;

/***
 * The screen that includes score from this play (+ perhaps highscore current game session?).
 * Always return to menu screen after this screen.
 */
public class GameOverScreen implements Screen {
    private static final String TAG = SplashScreen.class.getSimpleName();
    private static final float FONT_SCALE = 4.0f;
    private SpriteBatch batch;
    private BitmapFont textFont;
    private Texture backgroundTexture;
    private ScreenSwitcher screenSwitcher;
    private boolean playAgainSelected = true; // Flag to track whether "Play Again" is selected
    private ScoreManager scoreManager;
    private Vector2 birbPosition;
    private Vector2 groundPosition;


    public GameOverScreen(ScreenSwitcher screenSwitcher) {

        this.screenSwitcher = screenSwitcher;
    }

    public GameOverScreen(Vector2 birbPosition, Vector2 groundPosition) {
        this.birbPosition = birbPosition;
        this.groundPosition = groundPosition;
    }

    /**
     * Constructor. Initialize ScoreManager.
     */
    public GameOverScreen() {
        scoreManager = ScoreManager.getInstance();
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();
        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textFont.getData().setScale(FONT_SCALE);
    }

    /***
     * This method is called when the Application should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        backgroundTexture = new Texture("Bakgrund1.jpg");

        // Render background image
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);

        // Draw text "Gameover"
        // Draw your game over screen elements here
        textFont.draw(batch, "Game Over", Gdx.graphics.getWidth() / 4f,
               2 * Gdx.graphics.getHeight() / 2.5f, 0, Align.left, false);

        //Draw this sessions score and the highscore
        drawGameOverScores();

        // Handle input
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            playAgainSelected = !playAgainSelected; // Toggle selection between "Play Again" and "Exit"
        }
        // Draw menu options
        textFont.draw(batch, (playAgainSelected ? "> " : "") + "Play Again", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f - 100, 0, Align.left, false);
        textFont.draw(batch, (!playAgainSelected ? "> " : "") + "Exit", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f - 150, 0, Align.left, false);

        batch.end();
    }

    private void drawGameOverScores() {
        float textPadding = 50f;
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        // Draw scores with respect to the background position and dimensions
        // Draw scores
        textFont.draw(batch, "Score: " + currentScore, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f - 80);
        textFont.draw(batch, "High Score: " + highScore, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f - textPadding);
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
        Gdx.app.log(TAG, "dispose() called");
        batch.dispose();
    }

}