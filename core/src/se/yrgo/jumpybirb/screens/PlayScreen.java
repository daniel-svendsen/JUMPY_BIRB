package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import se.yrgo.jumpybirb.sprites.Birb;
import se.yrgo.jumpybirb.sprites.Obstacle;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.ScoreManager;

/***
 * The screen that runs the actual game, a round of the game.
 */
public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private Birb birb;
    private ScoreManager scoreManager;
    private Texture backgroundTexture;
    private BitmapFont textFont;
    private static final float TEXT_FONT_SCALE = 2.0f;
    private OrthographicCamera camera;
    private static final int OBSTACLE_COUNT = 4;
    private Array<Obstacle> obstacles;
    private static final int OBSTACLE_SPACING = 180; // Spacing between tubes horizontally
    private Texture groundTexture;
    private Vector2 groundPosition;
    private float obstacleSpeed;

    /***
     * This is used to tell which state the playScreen is in.
     * Not implemented yet...
     */
    public enum GameState {
        MENU, READY, RUNNING, GAMEOVER
    }

    /**
     * Constructor. Initialize ScoreManager.
     * For-loop for adding obstacles.
     */
    public PlayScreen() {
        scoreManager = ScoreManager.getInstance();
        obstacles = new Array<Obstacle>();

        for (int i = 1; i <= OBSTACLE_COUNT; i++) { // for loop for adding tubes
            obstacles.add(new Obstacle(i * (OBSTACLE_SPACING + Obstacle.OBSTACLE_WIDTH)));
        }
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("Bakgrund1.jpg");
        birb = new Birb();
        Gdx.input.setInputProcessor(new InputHandler(birb));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 800); // Adjust this to match your world width and height
        groundTexture = new Texture("Ground2.png");
        groundPosition = new Vector2(-300, 0);
        //obstacleSpeed = obstacles.get(0).getSpeed(); // Adjust this based on your game
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
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update birb
        birb.update(delta);

        // Update camera position to follow the birb
        camera.position.x = birb.getPosition().x;
        camera.update();

        // Set the SpriteBatch's projection matrix to the camera's combined matrix
        batch.setProjectionMatrix(camera.combined);

        // Move ground with obstacles
        groundPosition.x -= obstacleSpeed * delta;

        // Reset ground position when it goes off-screen
        if (groundPosition.x < -groundTexture.getWidth()) {
            groundPosition.x += groundTexture.getWidth();
        }

        // Begin batch
        batch.begin();

        batch.draw(backgroundTexture, camera.position.x - camera.viewportWidth / 2f, 0, camera.viewportWidth, camera.viewportHeight);

        float currentX = groundPosition.x;
        while (currentX < camera.position.x + camera.viewportWidth / 2f) {
            batch.draw(groundTexture, currentX, 0, groundTexture.getWidth(), groundTexture.getHeight());
            currentX += groundTexture.getWidth();
        }

        // Draw text and scores, passing the background coordinates and dimensions
        drawTextAndScores(camera.position.x - camera.viewportWidth / 2f, 0, camera.viewportWidth, camera.viewportHeight);

        // Draws the obstacles
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosTopObstacle().x > 320) {
                batch.draw(obstacle.getTopObstacle(), obstacle.getPosTopObstacle().x, obstacle.getPosTopObstacle().y);
                batch.draw(obstacle.getBottomObstacle(), obstacle.getPosBottomObstacle().x, obstacle.getPosBottomObstacle().y);
            }
        }

        // Render a new obstacle when an obstacle leaves the screen
        for (Obstacle obstacle : obstacles) {
            if (camera.position.x - (camera.viewportWidth / 2) > obstacle.getPosTopObstacle().x + obstacle.getTopObstacle().getWidth()) {
                obstacle.reposition(obstacle.getPosTopObstacle().x + (Obstacle.OBSTACLE_WIDTH + OBSTACLE_SPACING) * OBSTACLE_COUNT);
            }
        }

        // Draw birb
        batch.draw(birb.getTexture(), birb.getPosition().x, birb.getPosition().y);


        // End batch
        batch.end();
    }

    /**
     * TODO write Javadoc here
     *
     * @param backgroundX
     * @param backgroundY
     * @param backgroundWidth
     * @param backgroundHeight
     */
    private void drawTextAndScores(float backgroundX, float backgroundY, float backgroundWidth, float backgroundHeight) {
        float textPadding = 50f;
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        // Draw text and scores with respect to the background position and dimensions
        textFont.draw(batch, "Press Esc to go to Menu", backgroundX + backgroundWidth / 4f,
                backgroundY + backgroundHeight / 3f - textPadding, 0, Align.left, false);
        textFont.draw(batch, "Score: " + currentScore, backgroundX + 10, backgroundY + backgroundHeight - 10f);
        textFont.draw(batch, "High Score: " + highScore, backgroundX + 10, backgroundY + backgroundHeight - 10f - 50f);
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
        batch.dispose();
        backgroundTexture.dispose();
        textFont.dispose();
    }
}