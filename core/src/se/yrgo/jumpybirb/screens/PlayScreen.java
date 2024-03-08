package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import se.yrgo.jumpybirb.sprites.Birb;
import se.yrgo.jumpybirb.sprites.Ground;
import se.yrgo.jumpybirb.sprites.Obstacle;
import se.yrgo.jumpybirb.utils.ScoreManager;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;
import se.yrgo.jumpybirb.utils.Screens;

/***
 * The screen that runs the actual game, a round of the game.
 * Responsible for managing the game state and interactions between game objects.
 */
public class PlayScreen implements Screen {
    public static final String TAG = PlayScreen.class.getSimpleName();
    private static final float TEXT_FONT_SCALE = 2.0f;
    private SpriteBatch batch;
    private Birb birb;
    private ScoreManager scoreManager;
    private ScreenSwitcher screenSwitcher;
    private GameState currentGameState;
    private Texture backgroundTexture;
    private BitmapFont textFont;
    private Texture getReadyTexture;
    private OrthographicCamera camera;
    private Array<Obstacle> obstacles;
    private Texture groundTexture;
    private Vector2 groundPosition;
    private Ground ground;
    private Texture greenTexture; //TODO remove this after debugging
    private ShapeRenderer shapeRenderer; //TODO remove this after debugging

    /***
     * This is used to tell which state the playScreen is in.
     * Not implemented yet...
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
        this.screenSwitcher = screenSwitcher;
        currentGameState = GameState.READY;
        obstacles = Obstacle.createArray();
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

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        // Don't let the SplashScreen timer interrupt the PlayScreen
        SplashScreen.setPlayScreenDisplayed(true);

        batch = new SpriteBatch();
        backgroundTexture = new Texture("Bakgrund1.jpg");
        birb = new Birb(66, 64);
        shapeRenderer = new ShapeRenderer(); //TODO remove this after debugging

        // Initialize ground and groundPosition
        ground = new Ground(-300, 0, 100f);
        groundPosition = new Vector2(-300, 0);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 800); // Adjust this to match your world width and height
        groundTexture = new Texture("Ground2.png");
        groundPosition = new Vector2(-300, 0);
        getReadyTexture = new Texture("get-ready.png"); // placeholder get-ready image

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textFont.getData().setScale(TEXT_FONT_SCALE);

        //TODO remove this after debugging
        // Create a green pixmap
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        // Create a texture from the pixmap
        greenTexture = new Texture(pixmap);
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
                updateGameOverState(delta);
                break;
            default:
                break;
        }
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    private void updateReadyState(float delta) {
        Gdx.app.log(TAG, "GameState: READY");

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
        batch.end();
    }

    private void updateRunningState(float delta) {
        Gdx.app.log(TAG, "GameState: RUNNING");

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

        // Draw text and scores, passing the background coordinates and dimensions
        drawTextAndScores(camera.position.x - camera.viewportWidth / 2f, 0, camera.viewportWidth, camera.viewportHeight);

        // Update obstacles and draw them
        for (Obstacle obs : obstacles) {
            obs.update(camera);
            obs.draw(batch, camera);
            if (obs.isPassed() && !obs.isScored()) {
                scoreManager.updateScore();  // Call the scoring logic in ScoreManager
                obs.setScored(true);  // Mark the obstacle as scored to avoid multiple increments
                obs.checkPassed(birb.getPosition().x);
            }
        }


        // Reset ground position when it goes off-screen
        if (groundPosition.x < -groundTexture.getWidth()) {
            groundPosition.x += groundTexture.getWidth();
        }

        // Render birb bounds TODO remove this after debugging
        shapeRenderer.setProjectionMatrix(camera.combined);
        birb.renderBounds(shapeRenderer);


        // End batch
        batch.end();

        //TODO remove this after debugging
        // Render green bounding rectangles for obstacles
        // Set the color to green with 50% opacity
        batch.begin();
        batch.setColor(0f, 1f, 0f, 0.5f);
        for (Obstacle obstacle : obstacles) {
            // Draw bounding rectangles for top and bottom obstacles
            batch.draw(obstacle.getTopObstacle(), obstacle.getPosTopObstacle().x, obstacle.getPosTopObstacle().y);
            batch.draw(obstacle.getBottomObstacle(), obstacle.getPosBottomObstacle().x, obstacle.getPosBottomObstacle().y);

            // Draw bounding rectangles (green)
            batch.draw(greenTexture, obstacle.boundsTop.x, obstacle.boundsTop.y, obstacle.boundsTop.width, obstacle.boundsTop.height);
            batch.draw(greenTexture, obstacle.boundsBot.x, obstacle.boundsBot.y, obstacle.boundsBot.width, obstacle.boundsBot.height);
        }
        batch.setColor(Color.WHITE); // Reset color to white
        batch.end();
        //TODO remove code above

        Gdx.app.log(TAG, "Camera position: " + camera.position.x + ", " + camera.position.y);

        // Check for game over after drawing obstacles
        if (checkForGameOver(birb)) {
            setCurrentGameState(GameState.GAME_OVER);
        }
    }

    private void updateGameOverState(float delta) {
        if (checkForGameOver(birb)) {
            resetGame();
            setCurrentGameState(GameState.READY);
            Gdx.app.log(TAG, "GameState: GAME_OVER");
            screenSwitcher.switchToScreen(Screens.GAME_OVER);
        }
    }

    /**
     * Helper method to updateGameOverState
     *
     * @param player the Birb
     * @return true if player collided with an obstacle, false otherwise
     */
    public boolean checkForGameOver(Birb player) {
        // check if the player has collided with the ground
        if (birb.getPosition().y <= 37) { // ground.getHeight() + GROUND_OFFSET
            return true;
        }
        // iterate through obstacles to check for collision with the player
        for (Obstacle obstacle : obstacles) {
            if (obstacle.collidesWith(player.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public void resetGame() {
        // Reset necessary game elements (e.g., birb position, obstacles, score)
        birb.reset();
        scoreManager.reset();

        // Clear existing array of obstacles and generate new obstacles in it
        obstacles.clear();
        obstacles = Obstacle.createArray();
    }

    /**
     * TODO write Javadoc here
     */

    private void drawTextAndScores(float backgroundX, float backgroundY, float backgroundWidth, float backgroundHeight) {
        float textPadding = 50f;
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        // Draw text and scores with respect to the background position and dimensions
        textFont.draw(batch, "Press Esc to go to Menu", backgroundX + backgroundWidth / 4f,
                backgroundY + backgroundHeight / 3f - textPadding, 0, Align.left, false);
        textFont.draw(batch, "Score: " + currentScore, backgroundX + 370, backgroundY + backgroundHeight - 10f);
        textFont.draw(batch, "High Score: " + highScore, backgroundX + 370, backgroundY + backgroundHeight - 10f - 50f);
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
    @Override
    public void dispose() {
        // Dispose of resources
        ground.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        textFont.dispose();
        greenTexture.dispose(); //TODO remove this after debugging
        shapeRenderer.dispose(); //TODO remove this after debugging
    }
}
