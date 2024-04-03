package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.utils.*;


/***
 * The screen that shows highscore board.
 */
public class HighScoreScreen implements Screen {
    private static final String TAG = HighScoreScreen.class.getSimpleName();
    private Texture backButtonSelectedTexture;
    private ImageButton backButton;
    private boolean isBackButtonClicked = false;
    private Texture backgroundTexture;
    private Texture highscoreTitle;
    private SpriteBatch batch;
    private BitmapFont font;
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
        highscoreManager = HighscoreManager.getInstance();
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");

        backgroundTexture = new Texture("Background1.jpg");
        highscoreTitle = new Texture("Highscores.png");
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(TEXT_FONT_SCALE);

        // Texture for selected back button
        backButtonSelectedTexture = new Texture(Gdx.files.internal("Back-checked.png"));

        // Initialize back button
        backButton = new ImageButton(new TextureRegionDrawable(backButtonSelectedTexture));
        backButton.setSize(232f, 92f);
        float buttonY = Gdx.graphics.getHeight() / 5.5f;
        float buttonX = (Gdx.graphics.getWidth() - backButton.getWidth()) / 2f;
        backButton.setPosition(buttonX, buttonY);

        // Create click listener for the back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isBackButtonClicked = true;
            }
        });
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

        // Check if the back button is clicked
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = (float) Gdx.graphics.getHeight() - Gdx.input.getY();

            if (touchX >= backButton.getX() && touchX <= backButton.getX() + backButton.getWidth() &&
                    touchY >= backButton.getY() && touchY <= backButton.getY() + backButton.getHeight()) {
                isBackButtonClicked = true;
            }
        }

        // Render background image & header
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.draw(highscoreTitle, 0, 90);

        // Render high scores
        printHighScores();

        // Render back button
        backButton.draw(batch, 1);

        batch.end();

        // Switch to menu if the back button is clicked
        if (isBackButtonClicked) {
            JumpyBirb.getScreenSwitcher().switchToScreen(Screens.MENU);
            isBackButtonClicked = false;
        }
    }

    private void printHighScores() {
        Vector2 position = new Vector2(180, 600);

        // Ensure there are 10 spots in the high scores list
        for (int i = 0; i < 10; i++) {
            String playerName = "-----";
            int score = 0;

            if (i < highscoreManager.getHighScoreBoard().size()) {
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
        batch.dispose();
        font.dispose();
        backButtonSelectedTexture.dispose();
        highscoreTitle.dispose();
        backgroundTexture.dispose();
    }
}
