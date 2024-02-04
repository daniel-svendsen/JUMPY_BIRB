package se.yrgo.jumpybirb;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import se.yrgo.jumpybirb.screens.*;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;

public class JumpyBirb extends Game {
    SpriteBatch batch;
    Texture img;

    public static final String TAG = JumpyBirb.class.getName();

    SplashScreen splashScreen;
    MenuScreen menuScreen;
    HighScoreScreen highScoreScreen;
    PlayScreen playScreen;
    GameOverScreen gameOverScreen;

    @Override
    public void create() {
        // Set log level, on desktop, the messages are logged to the console.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.log(TAG, "create() called");

        splashScreen = new SplashScreen();
        menuScreen = new MenuScreen();
        setScreen(splashScreen);

        // We'll talk about this soon. This lets us hit spacebar to swap screens
        Gdx.input.setInputProcessor(new ScreenSwitcher(this));

    /*    try {
            // Your initialization code that might throw an exception
            batch = new SpriteBatch();
            img = new Texture("badlogic.jpg");

            // Other initialization code...
        } catch (Exception ex) {
            // Log the exception along with a message
            Gdx.app.error("JumpyBirb", "Error during initialization", ex);

            // Optionally, you might throw the exception again if you want to propagate it
            // throw e;
        }*/
    }

    @Override
    public void dispose() {
      /*  batch.dispose();
        img.dispose();*/

        Gdx.app.log(TAG, "dispose() called");
        super.dispose();
    }
}

