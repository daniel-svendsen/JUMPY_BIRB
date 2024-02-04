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
    public static final String TAG = SplashScreen.class.getName();
    private static final Float TEXT_FONT_SCALE = 2.0f;
    BitmapFont textFont;
    private SpriteBatch batch;
    private Texture playerTexture;
    private ScoreManager scoreManager;

    /***
     * This is used to tell which state the playScreen is in.
     * Not implemented yet...
     */
    public enum GameState {
        MENU, READY, RUNNING, GAMEOVER
    }

    public PlayScreen(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();
        // Change the birb texture here later :)
        playerTexture = new Texture("ugly-bird.png");

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textFont.getData().setScale(TEXT_FONT_SCALE);

    }

    @Override
    public void render(float delta) {
        // Set the background color to blue (0, 0, 1 is RGB for blue)
        Gdx.gl.glClearColor(0, 0.35f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Some placeholder text
        batch.begin();
        float textPadding = 50f; // Adjust the padding between text elements
        textFont.draw(batch, "Press Esc to go to Menu", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 3f - textPadding, 0, Align.left, false);

        batch.end();

        // Render bird image
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

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
