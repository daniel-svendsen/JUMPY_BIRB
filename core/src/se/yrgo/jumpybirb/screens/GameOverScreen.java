package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.utils.ScoreManager;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;

/***
 * The screen that includes score from this play (+ perhaps highscore current game session?).
 * Always return to menu screen after this screen.
 */
public class GameOverScreen implements Screen {
    private static final String TAG = SplashScreen.class.getSimpleName();
    private static final float FONT_SCALE = 3.5f;
    private SpriteBatch batch;
    private BitmapFont textFont;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator scoreNumbersFontGenerator;
    private BitmapFont gameOverFont;
    private BitmapFont scoreFont;
    private BitmapFont scoreNumbersFont;
    private BitmapFont playOrExitFont;

    private Texture backgroundTexture;
    private boolean playAgainSelected = true; // Flag to track whether "Play Again" is selected
    private ScoreManager scoreManager;


    // Constructor. Initialize ScoreManager.
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

        // Set up gameOverFont
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BRLNSDB.ttf"));
        scoreNumbersFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BRLNSDB.ttf"));
        FreeTypeFontParameter largeStyle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        largeStyle.size = 60;
        largeStyle.color = Color.valueOf("#ffda05");
        largeStyle.borderColor = Color.valueOf("#522f22");
        largeStyle.borderWidth = 5;

        FreeTypeFontParameter mediumStyle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mediumStyle.size = 42;
        mediumStyle.color = Color.valueOf("#ffda05");
        mediumStyle.borderColor = Color.valueOf("#522f22");
        mediumStyle.borderWidth = 5;

        FreeTypeFontParameter smallStyle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallStyle.size = 25;
        smallStyle.color = Color.valueOf("#ffda05");
        smallStyle.borderColor = Color.valueOf("#522f22");
        smallStyle.borderWidth = 5;

        FreeTypeFontParameter scoreNumbersStyle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreNumbersStyle.size = 25;
        scoreNumbersStyle.color = Color.valueOf("#ffda05");
        scoreNumbersStyle.borderColor = Color.valueOf("#522f22");
        scoreNumbersStyle.borderWidth = 5;

        gameOverFont = fontGenerator.generateFont(largeStyle);
        scoreFont = fontGenerator.generateFont(mediumStyle);
        scoreNumbersFont = scoreNumbersFontGenerator.generateFont(scoreNumbersStyle);
        playOrExitFont = fontGenerator.generateFont(mediumStyle);

        backgroundTexture = new Texture(Gdx.files.internal("Bakgrund1.jpg"));
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

        // Render background image
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);

        // Draw text "Gameover"
        // Draw your game over screen elements here
        gameOverFont.draw(batch, "Game Over", Gdx.graphics.getWidth() / 5f,
                2 * Gdx.graphics.getHeight() / 2.5f, 0, Align.left, false);

        //Draw this sessions score and the highscore
        drawGameOverScores();

        // Handle input
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            playAgainSelected = !playAgainSelected; // Toggle selection between "Play Again" and "Exit"
        }
        // Draw menu options
        playOrExitFont.draw(batch, (playAgainSelected ? "> " : "") + "Play Again", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f - 100, 0, Align.left, false);
        playOrExitFont.draw(batch, (!playAgainSelected ? "> " : "") + "Exit", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 2f - 150, 0, Align.left, false);

        batch.end();
    }

    private void drawGameOverScores() {
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        Table table = new Table();
        table.setPosition(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 2f); // Adjust position as needed

        // Add "High Score" label with scoreFont to the table
        Label highScoreLabel = new Label("High Score: ", new Label.LabelStyle(scoreFont, Color.WHITE));
        table.add(highScoreLabel).align(Align.left);

        // Add high score value with scoreNumbersFont to the table
        Label highScoreValueLabel = new Label(String.valueOf(highScore), new Label.LabelStyle(scoreNumbersFont, Color.WHITE));
        table.add(highScoreValueLabel).align(Align.left).padLeft(10).row();

        // Add padding between columns
        table.pad(40);

        // Add empty row for spacing
        table.row().height(scoreNumbersFont.getXHeight() * 3);

        // Add "Your score" label with scoreFont to the table
        Label yourScoreLabel = new Label("Your Score: ", new Label.LabelStyle(scoreFont, Color.WHITE));
        table.add(yourScoreLabel).align(Align.left);

        // Add current score value with scoreNumbersFont to the table
        Label yourScoreValueLabel = new Label(String.valueOf(currentScore), new Label.LabelStyle(scoreNumbersFont, Color.WHITE));
        table.add(yourScoreValueLabel).align(Align.left).padLeft(10).row();

        table.pack(); // Pack the table to adjust its size according to its content
        table.draw(batch, 1); // Draw the table onto the batch
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
        fontGenerator.dispose();
    }

}