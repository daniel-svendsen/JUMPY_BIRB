package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.utils.HighscoreManager;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.ScoreManager;

/***
 * The screen that shows highscore board.
 */
public class HighScoreScreen implements Screen {
    private Texture backgroundTexture;
    private Texture highscoreTitle;
    private SpriteBatch batch;
    private BitmapFont font;
    private ScoreManager scoreManager;
    private HighscoreManager highscoreManager;
    private InputHandler inputHandler;
    private static final float TEXT_FONT_SCALE = 2.0f;

    /**
     * Constructor
     */
    public HighScoreScreen(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        batch = new SpriteBatch();
        font = new BitmapFont();
        highscoreManager = new HighscoreManager();

    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        backgroundTexture = new Texture("Background1.jpg");
        highscoreTitle = new Texture("Highscores.png");
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(TEXT_FONT_SCALE);

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

        // Render background image & header
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.draw(highscoreTitle, 0, 90);

        // Render high scores
        printHighScores();

        batch.end();
    }

    private void printHighScores() {
        Vector2 position = new Vector2(180, 600);

        // Ensure there are 10 spots in the high scores list
        for (int i = 0; i < 10; i++) {
            String playerName = "-----";
            int score = 0;

            if (i < highscoreManager.getHighscores().size()) {
                // Get player name and score if available
                playerName = highscoreManager.getPlayerNameByIndex(i);
                score = highscoreManager.getHighscoreByIndex(i);
            }

            // Render position number (left-aligned)
            font.draw(batch, String.format("%d.", i + 1), position.x, position.y - i * 30);

            // Render player name (limited to 4 characters)
            font.draw(batch, playerName.substring(0, Math.min(playerName.length(), 4)), position.x + 50, position.y - i * 30);

            // Render score
            font.draw(batch, String.valueOf(score), position.x + 200, position.y - i * 30);
        }
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
