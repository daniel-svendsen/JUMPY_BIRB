package se.yrgo.jumpybirb;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;

/***
 * The main class of the application. Used as entry point for our game.
 */
public class JumpyBirb extends Game {

    public static final String TAG = JumpyBirb.class.getName();

    /**
     * This method gets called first (entry point)
     * once when the application is created.
     */
    @Override
    public void create() {
        // Set log level. On desktop the messages are logged to the console.
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.log(TAG, "create() called");

        Gdx.input.setInputProcessor(new ScreenSwitcher(this));
    }

    /**
     * This method gets called when the Application is shutting down.
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose() called");
        super.dispose();
    }

}

