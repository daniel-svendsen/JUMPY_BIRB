package se.yrgo.jumpybirb;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import se.yrgo.jumpybirb.screens.*;
import se.yrgo.jumpybirb.utils.ScoreManager;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;

public class JumpyBirb extends Game {
    SpriteBatch batch;
    Texture img;

    public static final String TAG = JumpyBirb.class.getName();

    private SplashScreen splashScreen;
    private MenuScreen menuScreen;
    private HighScoreScreen highScoreScreen;
    private PlayScreen playScreen;
    private GameOverScreen gameOverScreen;

    private ScoreManager scoreManager;

    @Override
    public void create() {
        // Set log level, on desktop, the messages are logged to the console.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.log(TAG, "create() called");

        scoreManager = new ScoreManager(); // Initialize the existing class variable

        splashScreen = new SplashScreen();
        menuScreen = new MenuScreen();
        playScreen = new PlayScreen(scoreManager);
        setScreen(splashScreen);

        Gdx.input.setInputProcessor(new ScreenSwitcher(this));
    }

    @Override
    public void dispose() {
      /*  batch.dispose();
        img.dispose();*/

        Gdx.app.log(TAG, "dispose() called");
        super.dispose();
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }
}

