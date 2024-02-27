package se.yrgo.jumpybirb.utils;



import com.badlogic.gdx.Gdx;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.screens.PlayScreen;

/***
 * This class is used for switching between screens in the game.
 * It has all the logic to determine which screen should be displayed by
 * checking input from the player accordingly.
 */

public final class ScreenSwitcher {
    public static final String TAG = ScreenSwitcher.class.getSimpleName();
    public final String debuggingName;
    private final JumpyBirb gameSession;
    private Screens currentScreen;
    private PlayScreen playScreen;
    private boolean splashScreenFinished;

    /***
     * Constructor
     * @param gameSession the current game session
     */
    public ScreenSwitcher(JumpyBirb gameSession, String debuggingName) {
        this.gameSession = gameSession;
        this.debuggingName = debuggingName;
        this.splashScreenFinished = false;
        this.currentScreen = Screens.SPLASH;
        playScreen = new PlayScreen();
        switchToScreen(Screens.SPLASH);
    }

    public void setSplashScreenFinished(boolean trueOrFalse) {
        splashScreenFinished = trueOrFalse;
    }

    public boolean isSplashScreenFinished() {
        return splashScreenFinished;
    }

    public Screens getCurrentScreen() {
        return currentScreen;
    }

    public PlayScreen getPlayScreen() {
        return playScreen;
    }

    public String getDebuggingName() {
        return debuggingName;
    }

    /***s
     * This method is a helper method to the keyUp method.
     * @param screen to switch to
     */
    public void switchToScreen(Screens screen) {
        currentScreen = screen;
        gameSession.setScreen(screen.getScreenInstance());
        Gdx.app.log(TAG, debuggingName + " switched to " + screen);
    }

    public void gameOver() {
        switchToScreen(Screens.GAME_OVER);
    }

}
