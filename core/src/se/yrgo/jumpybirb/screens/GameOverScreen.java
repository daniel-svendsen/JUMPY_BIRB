package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.utils.*;
import se.yrgo.jumpybirb.utils.HighscoreManager;
import se.yrgo.jumpybirb.utils.ScoreManager;

import static com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;
import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;

/***
 * The screen that includes score from this play (+ perhaps highscore current game session?).
 * Always return to menu screen after this screen.
 */
public class GameOverScreen implements Screen, GameOverListener {
    private static final String TAG = SplashScreen.class.getSimpleName();
    private GameOverScreen.HighScoreState currentState;
    private SpriteBatch batch;
    private Texture playAgainButtonTexture;
    private Texture exitButtonTexture;
    private Texture playAgainButtonSelectedTexture;
    private Texture exitButtonSelectedTexture;
    private ImageButton playAgainButton;
    private ImageButton exitButton;
    private boolean buttonStylesInitialized;
    int currentSelectedButtonIndex = 0;
    private Stage stage;
    private final ScreenSwitcher screenSwitcher;
    private final InputHandler inputHandler;
    private FreeTypeFontGenerator fontGenerator;
    private BitmapFont scoreFont;
    private BitmapFont scoreNumbersFont;
    private Texture backgroundTexture;
    private Texture gameOverHeaderImage;
    private Table gameOverScoresTable;
    private final ScoreManager scoreManager;
    private final int gameScore;
    private final HighscoreManager highscoreManager;
    private String playerName = ""; // Variable to store player name

    /***
     * This is used to tell which state the GameOverScreen is in.
     */
    public enum HighScoreState {
        LOSER, WINNER
    }

    public GameOverScreen(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        screenSwitcher = JumpyBirb.getScreenSwitcher();
        scoreManager = ScoreManager.getInstance();
        highscoreManager = HighscoreManager.getInstance();
        this.gameScore = scoreManager.getScore();
        this.currentState = GameOverScreen.HighScoreState.LOSER;
    }

    public GameOverScreen.HighScoreState getCurrentState() {
        return currentState;
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        batch = new SpriteBatch();

        // Textures for normal buttons
        playAgainButtonTexture = new Texture(Gdx.files.internal("PlayAgainButton.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("ExitButton.png"));

        // Texture for selected buttons
        playAgainButtonSelectedTexture = new Texture(Gdx.files.internal("PlayAgainButton-checked.png"));
        exitButtonSelectedTexture = new Texture(Gdx.files.internal("ExitButton-checked.png"));

        // Initialize button styles and instances
        initializeButtonsAndStyles();

        // Create the stage for allowing buttons to be clickable with ClickListeners
        stage = new Stage(new ScreenViewport());

        gameOverScoresTable = new Table();
        gameOverScoresTable.setPosition(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 2f);

        // Create a table to hold the buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center().bottom().padBottom(Gdx.graphics.getHeight() * 0.08f); // Adjust Y position here
        stage.addActor(buttonTable);

        // Add the buttons to the table with padding
        float padding = 20f; // Adjust padding between buttons
        buttonTable.add(playAgainButton).padBottom(padding).size(232f, 92f).row();
        buttonTable.add(exitButton).padBottom(padding).size(232f, 92f).row();

        // Get the input handling working correctly with multiplexer
        configureInputMultiplexer();

        // Add click listeners to the buttons
        addClickListenersToGameOverButtons();

        // Set up fonts
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BRLNSDB.ttf"));

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

        scoreFont = fontGenerator.generateFont(mediumStyle);
        scoreNumbersFont = fontGenerator.generateFont(scoreNumbersStyle);

        // Set up background images
        backgroundTexture = new Texture("BackGround1.jpg");
        gameOverHeaderImage = new Texture("GameOver.png");
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

        // Draw GameOver image over background
        batch.draw(gameOverHeaderImage, 0, 0);

        //Draw this sessions score and the highscore
        drawGameOverScores();


        // If the player's score reaches a certain threshold, prompt for name input
        int currentScore = scoreManager.getScore();
        if (currentScore > highscoreManager.getLowestScore()) {
            handlePlayerNameInput();
        }

        // Draw player name on the screen
        drawPlayerInputName();

        currentSelectedButtonIndex = inputHandler.getSelectedButtonIndexGameOver();
        updateButtonStyles();

        // Draw the stage
        stage.act(delta);
        stage.draw();

        batch.end();
    }

    private void initializeButtonsAndStyles() {
        if (!buttonStylesInitialized) {
            ImageButtonStyle playAgainButtonStyle = new ImageButtonStyle();
            playAgainButtonStyle.up = new TextureRegionDrawable(playAgainButtonTexture);
            playAgainButtonStyle.checked = new TextureRegionDrawable(playAgainButtonSelectedTexture);

            ImageButtonStyle exitButtonStyle = new ImageButtonStyle();
            exitButtonStyle.up = new TextureRegionDrawable(exitButtonTexture);
            exitButtonStyle.checked = new TextureRegionDrawable(exitButtonSelectedTexture);

            playAgainButton = new ImageButton(playAgainButtonStyle);
            exitButton = new ImageButton(exitButtonStyle);

            buttonStylesInitialized = true;
        }
    }

    /**
     * This method is used to get the input handling of
     * the stage in MenuScreen to work properly with the ClickListeners.
     * Without this configuration, the player is unable to click the menu buttons.
     */
    private void configureInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * This method is used to allow buttons to be clickable.
     * Here we can set the action we want for each button in the menu.
     */
    private void addClickListenersToGameOverButtons() {
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(TAG, "Play Again Button clicked");
                playAgainButtonClicked();
                Gdx.input.setInputProcessor(inputHandler);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(TAG, "Exit Button clicked");
                Gdx.app.exit();
            }
        });
    }

    /**
     * Action to be done if the "PLAY AGAIN" button is clicked.
     */
    @Override
    public void playAgainButtonClicked() {
        screenSwitcher.switchToScreen(Screens.PLAY);
        Gdx.app.log(TAG, "playAgainButtonClicked() called");
    }

    private void drawGameOverScores() {
        gameOverScoresTable.clear();

        int currentScore = scoreManager.getScore();
        int topHighScore = highscoreManager.getHighestScore();

        Table table = new Table();
        table.setPosition(Gdx.graphics.getWidth() / 10f, Gdx.graphics.getHeight() / 2f); // Adjust position as needed

        // Add "High Score" label with scoreFont to the table
        Label highScoreLabel = new Label("High Score: ", new Label.LabelStyle(scoreFont, Color.WHITE));
        table.add(highScoreLabel).align(Align.left);

        // Add high score value with scoreNumbersFont to the table
        Label highScoreValueLabel = new Label(String.valueOf(topHighScore), new Label.LabelStyle(scoreNumbersFont, Color.WHITE));
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
        table.row().height(scoreNumbersFont.getXHeight() * 3);
    }

    private void handlePlayerNameInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (!playerName.isEmpty()) { // If player name is not empty
                int currentScore = scoreManager.getScore();
                highscoreManager.addHighscore(currentScore); // Add score to highscores
                //scoreManager.resetScore(); // Reset score for next game
                playerName = ""; // Reset player name for next game
                // Go back to menu screen
                // You can implement this transition as per your game's structure
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && !playerName.isEmpty()) {
            playerName = playerName.substring(0, playerName.length() - 1); // Remove last character
        } else if (playerName.length() < 4) { // Limit player name to 4 characters
            for (int i = 29; i >= 0; i--) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.A + i)) {
                    playerName += (char) ('A' + i); // Add character based on key pressed
                }
            }
        }
    }

    private void drawPlayerInputName() {
        // Draw player name at a specific position on the screen
        scoreNumbersFont.draw(batch, "Enter your name: " + playerName, Gdx.graphics.getWidth() / 5f,
                Gdx.graphics.getHeight() / 2.1f, 0, Align.left, false);
    }

    /**
     * This method is used to update the currently selected button
     * if the player traverses the menu with keyboard.
     * Texture for selected button is an image file instantiated
     * in the show method.
     */
    private void updateButtonStyles() {
        // Update button styles only when the selected button changes
        switch (currentSelectedButtonIndex) {
            case 0:
                playAgainButton.setChecked(true);
                exitButton.setChecked(false);
                break;
            case 1:
                exitButton.setChecked(true);
                playAgainButton.setChecked(false);
                break;
            default:
                break;
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
        Gdx.app.log(TAG, "dispose() called");
        batch.dispose();
        fontGenerator.dispose();
        playAgainButtonTexture.dispose();
        exitButtonTexture.dispose();
        playAgainButtonSelectedTexture.dispose();
        exitButtonSelectedTexture.dispose();
        backgroundTexture.dispose();
        gameOverHeaderImage.dispose();
        stage.dispose();
    }

}