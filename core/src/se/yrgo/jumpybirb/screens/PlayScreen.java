package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.sprites.Birb;
import se.yrgo.jumpybirb.sprites.Obstacle;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.ScoreManager;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private Birb birb;
    private ScoreManager scoreManager;
    private Texture backgroundTexture;
    private BitmapFont textFont;
    private static final float TEXT_FONT_SCALE = 2.0f;
    private OrthographicCamera camera;

    public PlayScreen() {
        scoreManager = ScoreManager.getInstance();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("Bakgrund1.jpg");
        birb = new Birb();
        Gdx.input.setInputProcessor(new InputHandler(birb));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 800); // Adjust this to match your world width and height

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textFont.getData().setScale(TEXT_FONT_SCALE);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update birb
        birb.update(delta);

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

        // Update camera position to follow the birb
        camera.position.x = birb.getPosition().x;
        camera.update();

        // Set the SpriteBatch's projection matrix to the camera's combined matrix
        batch.setProjectionMatrix(camera.combined);

        // Begin batch
        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 0, 600, 800); // Adjust this to match your world width and height

        // Draw birb
        batch.draw(birb.getTexture(), birb.getPosition().x, birb.getPosition().y);

        // Draw text and scores
        drawTextAndScores();

        // End batch
        batch.end();
    }

    private void drawTextAndScores() {
        float textPadding = 50f;
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        textFont.draw(batch, "Press Esc to go to Menu", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 3f - textPadding, 0, Align.left, false);
        textFont.draw(batch, "Score: " + currentScore, 10, Gdx.graphics.getHeight() - 10f);
        textFont.draw(batch, "High Score: " + highScore, 10, Gdx.graphics.getHeight() - 10f - 50f);
    }

    @Override
    public void resize(int width, int height) {
        // Update the camera's viewport to match the screen size
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Dispose of resources
        batch.dispose();
        backgroundTexture.dispose();
        textFont.dispose();
    }
}