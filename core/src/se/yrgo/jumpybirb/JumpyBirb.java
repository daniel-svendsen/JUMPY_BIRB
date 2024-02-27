package se.yrgo.jumpybirb;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import se.yrgo.jumpybirb.screens.SplashScreen;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;
import se.yrgo.jumpybirb.utils.Screens;

/***
 * The main class of the application. Used as entry point for our game.
 */
public class JumpyBirb extends Game {

    public static final String TAG = JumpyBirb.class.getName();
    private static ScreenSwitcher screenSwitcher;

    /**
     * This method gets called first (entry point)
     * once when the application is created.
     */
    @Override
    public void create() {
        // Set log level. On desktop the messages are logged to the console.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.log(TAG, "create() called");
        initializeGame(this);
        setScreen(new SplashScreen());
        Gdx.app.log(TAG, "create() ended");
    }

    private static void initializeGame(JumpyBirb gameSession) {
        screenSwitcher = new ScreenSwitcher(gameSession);
        InputHandler inputHandler = new InputHandler(gameSession, screenSwitcher);
        Gdx.input.setInputProcessor(inputHandler);
    }

    /**
     * This method gets called when the Application is shutting down.
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose() called");
        super.dispose();
    }

    public static ScreenSwitcher getScreenSwitcher() {
        return screenSwitcher;
    }
}

