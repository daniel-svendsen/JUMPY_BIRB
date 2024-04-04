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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.utils.*;

import static com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;
import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;
import static com.badlogic.gdx.scenes.scene2d.ui.TextField.*;

/***
 * The screen that includes score from this play (+ perhaps highscore current game session?).
 * Always return to menu screen after this screen.
 */
public class GameOverScreen implements Screen, GameOverListener {
    private static final String TAG = GameOverScreen.class.getSimpleName();
    private GameOverScreen.HighScoreState currentState;
    private TextField playerNameInputTextField;
    private Label playerNameMessageLabel;
    private TextureRegionDrawable cursor;
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
    private BitmapFont inputPlayerNameFont;
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
        NEUTRAL, WINNER
    }

    public GameOverScreen(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        screenSwitcher = JumpyBirb.getScreenSwitcher();
        scoreManager = ScoreManager.getInstance();
        highscoreManager = HighscoreManager.getInstance();
        this.gameScore = scoreManager.getScore();
        this.currentState = GameOverScreen.HighScoreState.NEUTRAL;
    }

    public GameOverScreen.HighScoreState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(HighScoreState currentState) {
        this.currentState = currentState;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public HighscoreManager getHighscoreManager() {
        return highscoreManager;
    }

    public TextField getPlayerNameInputTextField() {
        return playerNameInputTextField;
    }

    public Stage getStage() {
        return stage;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AtlantisInternational-jen0.ttf"));

        FreeTypeFontParameter mediumStyle = new FreeTypeFontParameter();
        mediumStyle.size = 42;
        mediumStyle.color = Color.valueOf("#ffda05");
        mediumStyle.borderColor = Color.valueOf("#522f22");
        mediumStyle.borderWidth = 2;

        FreeTypeFontParameter scoreNumbersStyle = new FreeTypeFontParameter();
        scoreNumbersStyle.size = 38;
        scoreNumbersStyle.color = Color.valueOf("#ffda05");
        scoreNumbersStyle.borderColor = Color.valueOf("#522f22");
        scoreNumbersStyle.borderWidth = 2;

        // Define the font parameters
        FreeTypeFontParameter playerStyle = new FreeTypeFontParameter();
        playerStyle.size = 38; // Set the font size

        // Generate the BitmapFont
        inputPlayerNameFont = fontGenerator.generateFont(playerStyle);

        scoreFont = fontGenerator.generateFont(mediumStyle);
        scoreNumbersFont = fontGenerator.generateFont(scoreNumbersStyle);

        loadCursor();
        setupTextField();
        setupLabelForTextField();

        // Set up background images
        backgroundTexture = new Texture("BackGround1.jpg");
        gameOverHeaderImage = new Texture("GameOver.png");

        // If the player's score reaches a certain threshold, prompt for name input
        if (highscoreManager.isScoreQualifiedForHighScore(gameScore)) {
            currentState = HighScoreState.WINNER;
            // Update input field and label
            Gdx.app.log(TAG, "Showing input field and label");
            playerNameInputTextField.setVisible(true);
            stage.setKeyboardFocus(playerNameInputTextField);
            playerNameMessageLabel.setVisible(true);
        } else {
            currentState = HighScoreState.NEUTRAL;
            Gdx.app.log(TAG, "Hiding input field and label");
            playerNameInputTextField.setVisible(false);
            playerNameMessageLabel.setVisible(false);
        }
    }

    private void loadCursor() {
        Texture cursorTexture = new Texture(Gdx.files.internal("cursor.png"));
        TextureRegion cursorRegion = new TextureRegion(cursorTexture);
        cursor = new TextureRegionDrawable(cursorRegion);
    }

    private void setupTextField() {
        // Create text field style
        TextFieldStyle textFieldStyle = new TextFieldStyle();

        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("1x1-transparent.png")))); // Set white background
        textFieldStyle.font = inputPlayerNameFont;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.focusedFontColor = Color.BLUE;
        textFieldStyle.messageFont = scoreFont;
        textFieldStyle.messageFontColor = Color.RED;
        textFieldStyle.cursor = cursor;

        // Set up text field
        playerNameInputTextField = new TextField("", textFieldStyle);
        playerNameInputTextField.setSize(100, 40); // Adjust size as needed
        playerNameInputTextField.setPosition(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 2.3f); // Adjust position as needed
        playerNameInputTextField.getStyle().background.setLeftWidth(10); // Set left margin
        playerNameInputTextField.getStyle().background.setRightWidth(10); // Set right margin
        playerNameInputTextField.setAlignment(Align.center); // Center align text

        // Set maximum character limit
        playerNameInputTextField.setMaxLength(4);

        // Add the text field to the stage
        stage.addActor(playerNameInputTextField);

        // Add input listener to the text field
        playerNameInputTextField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    Gdx.app.log(TAG, "Enter key pressed in text field");
                    handlePlayerNameInputForHighScore();
                    return true;
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.log(TAG, "Escape key pressed in text field");
                    screenSwitcher.switchToScreen(Screens.MENU);
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (Character.isLetterOrDigit(character)) {
                    // Append the character to the playerName variable
                    Gdx.app.log(TAG, "Character typed: " + character); // Log the typed character
                    playerName += character;
                    return true; // Consume the event
                } else if (character == '\b') {
                    Gdx.app.log(TAG, "Backspace key pressed"); // Log the Backspace key press event
                    // Handle backspace (optional)
                    if (!playerName.isEmpty()) {
                        playerName = playerName.substring(0, playerName.length() - 1);
                    }
                    return true; // Consume the event
                }
                return false; // Let the event propagate if not handled
            }
        });
    }

    private void handlePlayerNameInputForHighScore() {
        try {
            // Get the player name from the GameOverScreen or wherever it's stored
            Gdx.app.log(TAG, "Player Name Entered for High Score: " + playerName);
            Gdx.app.log(TAG, "Score to be saved to highScore: " + scoreManager.getScore());

            if (!playerName.isEmpty() && highscoreManager.isScoreQualifiedForHighScore(gameScore)) {
                // Here, you can save the player name and score to the high score manager
                highscoreManager.addHighscore(gameScore, playerName);
                currentState = HighScoreState.NEUTRAL;
                Gdx.app.log(TAG, "Now hiding input field and label again");
                playerNameInputTextField.setVisible(false);
                playerNameMessageLabel.setVisible(false);
                stage.setKeyboardFocus(null);
                setPlayerName("");
            } else {
                // Handle the case where the player name is empty
                // This could be due to invalid input or other reasons
                Gdx.app.error(TAG, "Empty player name provided for high score submission.");
            }
        } catch (Exception e) {
            // Catch any exceptions that might occur during high score submission
            Gdx.app.error(TAG, "Exception occurred during high score submission: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void setupLabelForTextField() {
        // Draw the text "Enter your name" at a specific position on the screen
        playerNameMessageLabel = new Label("Enter your name: ", new Label.LabelStyle(scoreNumbersFont, Color.WHITE));
        float labelWidth = playerNameMessageLabel.getWidth();
        float labelHeight = playerNameMessageLabel.getHeight();
        float textFieldHeight = playerNameInputTextField.getHeight();

        // Position the label to the left of the text field
        float labelX = playerNameInputTextField.getX() - labelWidth - 10; // Adjust 10 as needed for spacing
        float labelY = playerNameInputTextField.getY() + (textFieldHeight - labelHeight) / 2f;
        playerNameMessageLabel.setPosition(labelX, labelY);

        // Add the label to the stage
        stage.addActor(playerNameMessageLabel);
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
        batch.end();


        //Draw this sessions score and the highscore
        batch.begin();
        drawGameOverScores();
        batch.end();

        // Draw the stage
        stage.act(delta);
        stage.draw();

        currentSelectedButtonIndex = inputHandler.getSelectedButtonIndexGameOver();
        updateButtonStyles();
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