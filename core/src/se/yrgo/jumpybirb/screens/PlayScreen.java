package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import se.yrgo.jumpybirb.sprites.Birb;
import se.yrgo.jumpybirb.sprites.Ground;
import se.yrgo.jumpybirb.sprites.Obstacle;
import se.yrgo.jumpybirb.utils.HighscoreManager;
import se.yrgo.jumpybirb.utils.ScoreManager;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;
import se.yrgo.jumpybirb.utils.Screens;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;

/***
 * The screen that runs the actual game, a round of the game.
 * Responsible for managing the game state and interactions between game objects.
 */
public class PlayScreen implements Screen {
    public static final String TAG = PlayScreen.class.getSimpleName();
    private static final float TEXT_FONT_SCALE = 2.0f;
    private static Birb birb;
    private SpriteBatch batch;
    private ScoreManager scoreManager;
    private int currentScore;
    private HighscoreManager highscoreManager;
    private int topScore;
    private ScreenSwitcher screenSwitcher;
    private GameState currentGameState;
    private Texture backgroundTexture;
    private BitmapFont scoreFont;
    private BitmapFont countDownFont;
    private FreeTypeFontGenerator fontGenerator;
    private Texture getReadyTexture;
    private float getReadyCountDownTimer = 3f;
    private float delayTimerAfterCountDown;
    private boolean countDownFinished = false;
    private OrthographicCamera camera;
    private Array<Obstacle> obstacles;
    private Texture groundTexture;
    private Vector2 groundPosition;
    private Ground ground;
    private Sound gameOverSound;

    /***
     * This is used to tell which state the playScreen is in.
     */
    public enum GameState {
        MENU, READY, RUNNING, GAME_OVER
    }

    /**
     * Constructor. Initialize ScoreManager.
     * For-loop for adding obstacles.
     */
    public PlayScreen(ScreenSwitcher screenSwitcher) {
        birb = new Birb(66, 64);
        scoreManager = ScoreManager.getInstance();
        highscoreManager = HighscoreManager.getInstance();
        this.screenSwitcher = screenSwitcher;
        currentGameState = GameState.READY;
        obstacles = Obstacle.createArray();
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        // Don't let the SplashScreen timer interrupt the PlayScreen
        SplashScreen.setPlayScreenDisplayed(true);

        batch = new SpriteBatch();
        backgroundTexture = new Texture("Background1.jpg");
        birb = new Birb(66, 64);
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("death-sound.ogg"));

        // Initialize ground and groundPosition
        ground = new Ground(-300, 0, 100f);
        groundPosition = new Vector2(-300, 0);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 800); // Adjust this to match your world width and height
        groundTexture = new Texture("Ground2.png");
        groundPosition = new Vector2(-300, 0);
        getReadyTexture = new Texture("GetReady2.png"); // placeholder get-ready image

        scoreFont = new BitmapFont();
        scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        scoreFont.getData().setScale(TEXT_FONT_SCALE);

        // Set up countdown font
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CrackerWinter-VGLPl.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 120;
        fontParameter.color = Color.valueOf("#ffda05");
        fontParameter.borderColor = Color.valueOf("#522f22");
        fontParameter.borderWidth = 5;
        countDownFont = fontGenerator.generateFont(fontParameter);
    }

    public Birb getPlayerBirb() {
        return birb;
    }

    /***
     * This method is called when the Application should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (currentGameState) {
            case READY:
                // Handle ready state
                updateReadyState(delta);
                break;
            case RUNNING:
                // Handle running state
                updateRunningState(delta);
                break;
            case GAME_OVER:
                // Handle game over state
                resetGame();
                Gdx.app.log(TAG, "ResetGame called");
                updateGameOverState(delta);
                break;
            default:
                break;
        }
    }


    /**
     * This method is used in the InputHandler class
     *
     * @param currentGameState the GameState to update to
     * @see se.yrgo.jumpybirb.utils.InputHandler
     */
    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    private void updateReadyState(float delta) {
        // Reset score from before
        scoreManager.reset();

        // Render the assets without updating their positions
        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        // Draw background
        batch.draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);

        // Draw ground
        ground.render(batch, camera);

        // Draw birb at its initial position
        batch.draw(birb.getTexture(), birb.getInitialPosition()[0], birb.getInitialPosition()[1]);

        // Draw obstacles at their initial positions
        for (Obstacle obs : obstacles) {
            obs.draw(batch, camera);
        }
        // End batch
        batch.end();

        // Draw the "get-ready" image
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        float readyImagePositionX = (camera.viewportWidth - getReadyTexture.getWidth()) / 2f;
        float readyImagePositionY = (camera.viewportHeight - getReadyTexture.getHeight()) / 2f;
        batch.draw(getReadyTexture, readyImagePositionX, readyImagePositionY);

        // Countdown logic
        if (!countDownFinished) {
            getReadyCountDownTimer -= delta;
            if (getReadyCountDownTimer <= 0) {
                displayGoText();
                countDownFinished = true;
                delayTimerAfterCountDown = 1f; // Start the delay timer after "GO!" is displayed
            } else {
                displayCountdownText();
            }
        } else {
            // Delay timer after "GO!" is displayed
            delayTimerAfterCountDown -= delta;
            displayGoText();
            if (delayTimerAfterCountDown <= 0) {
                setCurrentGameState(GameState.RUNNING);
            }
        }

        batch.end();
    }

    private void displayCountdownText() {
        int countdownNumber = (int) Math.ceil(getReadyCountDownTimer); // Round up to nearest integer
        String textToDisplay = countdownNumber > 0 ? String.valueOf(countdownNumber) : ""; // Ensure not to display negative numbers
        countDownFont.draw(batch, textToDisplay, camera.viewportWidth / 2f, camera.viewportHeight / 1.75f, 0, Align.center, false);
    }

    private void displayGoText() {
        countDownFont.draw(batch, "GO!", camera.viewportWidth / 2f, camera.viewportHeight / 1.75f, 0, Align.center, false);
    }

    private void updateRunningState(float delta) {
        // Update game logic while in the running state (e.g., birb movement, obstacle movement)
        // Update birb
        birb.update(delta);

        // Update camera position to follow the birb
        camera.position.x = birb.getPosition().x;
        camera.update();

        // Set the SpriteBatch's projection matrix to the camera's combined matrix
        batch.setProjectionMatrix(camera.combined);

        // Begin batch
        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, camera.position.x - camera.viewportWidth / 2f, 0, camera.viewportWidth, camera.viewportHeight);

        // Draw and update ground
        ground.update(delta);
        ground.render(batch, camera);

        // Draw birb and update the position of the birb's sprite
        batch.draw(birb.getTexture(), birb.getPosition().x, birb.getPosition().y);

        // Update obstacles and draw them
        for (Obstacle obs : obstacles) {
            obs.update(camera);
            obs.checkPassed(birb.getPosition().x);
            obs.draw(batch, camera);
            if (obs.isPassed() && !obs.isScored()) {
                scoreManager.updateScore();  // Call the scoring logic in ScoreManager
                obs.setScored(true);  // Mark the obstacle as scored to avoid multiple increments
            }
        }


        // Reset ground position when it goes off-screen
        if (groundPosition.x < -groundTexture.getWidth()) {
            groundPosition.x += groundTexture.getWidth();
        }

        // Draw text and scores, passing the background coordinates and dimensions
        drawTextAndScores(camera.position.x - camera.viewportWidth / 2f, 0, camera.viewportWidth, camera.viewportHeight);

        // End batch
        batch.end();

        // Check for game over after drawing obstacles
        if (checkForGameOver(birb)) {
            gameOverSound.play();
            setCurrentGameState(GameState.GAME_OVER);
        }
    }

    private void updateGameOverState(float delta) {
        Gdx.app.log(TAG, "GameState: GAME_OVER");
        screenSwitcher.switchToScreen(Screens.GAME_OVER);
    }

    /**
     * Helper method to updateGameOverState
     *
     * @param player the Birb
     * @return true if player collided with an obstacle, false otherwise
     */
    public boolean checkForGameOver(Birb player) {
        if (player.getPosition().y <= 37) {
            return true;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.collidesWith(player.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public void resetGame() {
        // Reset necessary game elements (e.g., birb position, obstacles)
        birb.reset();

        // Clear existing array of obstacles and generate new obstacles in it
        obstacles.clear();
        obstacles = Obstacle.createArray();
    }


    private void drawTextAndScores(float backgroundX, float backgroundY, float backgroundWidth, float backgroundHeight) {
        float textPadding = 50f;
        currentScore = scoreManager.getScore();
        topScore = highscoreManager.getHighestScore();

        // Draw text and scores with respect to the background position and dimensions
        scoreFont.draw(batch, "High Score: " + topScore, backgroundX + 370, backgroundY + backgroundHeight - 10f);
        scoreFont.draw(batch, "Score: " + currentScore, backgroundX + 370, backgroundY + backgroundHeight - 10f  - 50f);
    }

    /***
     * This method is called when the Application is resized,
     * which can happen at any point during a non-paused state.
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        // Update the camera's viewport to match the screen size
        camera.setToOrtho(false, width, height);
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

    // Dispose of resources
    @Override
    public void dispose() {
        ground.dispose();
        batch.dispose();
        gameOverSound.dispose();
        backgroundTexture.dispose();
        fontGenerator.dispose();
        groundTexture.dispose();
        getReadyTexture.dispose();
    }
}
