package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.screens.GameOverScreen;
import se.yrgo.jumpybirb.screens.HighScoreScreen;
import se.yrgo.jumpybirb.screens.MenuScreen;
import se.yrgo.jumpybirb.screens.PlayScreen;
import se.yrgo.jumpybirb.screens.SplashScreen;

/***
 * An enum class that holds a fixed set of all available screens for the game.
 * Each enumeration constant is an object of its Screen class.
 * It currently has five constants, each associated with a specific screen class.
 * This class is used by the ScreenSwitcher to switch between screens.
 */
public enum Screens {
    SPLASH(SplashScreen.class),
    MENU(MenuScreen.class),
    PLAY(PlayScreen.class),
    GAME_OVER(GameOverScreen.class),
    HIGH_SCORE(HighScoreScreen.class);

    private final Class<? extends Screen> screen;

    /***
     * Constructor
     * @param screen the screen class that get assigned to the screen field.
     */
    Screens(Class<? extends Screen> screen) {
        this.screen = screen;
    }

    /***
     * This method is called to create an instance of a chosen screen.
     * @return an instance of the screen
     * only returns null in situations where instantiation fails or
     * an exception occurs.
     */
    public Screen getScreenInstance() {
        try {
            Screen instance = switch (this) {
                case SPLASH -> new SplashScreen();
                case MENU -> new MenuScreen(JumpyBirb.getInputHandler());
                case PLAY -> new PlayScreen(JumpyBirb.getScreenSwitcher());
                case GAME_OVER -> new GameOverScreen(JumpyBirb.getInputHandler());
                case HIGH_SCORE -> new HighScoreScreen(JumpyBirb.getInputHandler());
            };

            Gdx.app.log("Screens", "Instance of " + screen.getSimpleName() + " created successfully");
            return instance;
        } catch (Exception ex) {
            Gdx.app.error("Screens", "Error creating instance of " + screen.getSimpleName(), ex);
            return null;
        }
    }

}