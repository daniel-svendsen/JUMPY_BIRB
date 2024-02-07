package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.utils.Timer;
import se.yrgo.jumpybirb.JumpyBirb;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/***
 * This class is used for switching between screens in the game.
 * It has all the logic to determine which screen should be displayed by
 * checking input from the player accordingly.
 */

public class ScreenSwitcher extends InputAdapter {
    private final JumpyBirb gameSession;
    private Screens currentScreen;
    private boolean splashScreenFinished;

    /***
     * Constructor
     * @param gameSession the current game session
     */
    public ScreenSwitcher(JumpyBirb gameSession) {
        this.gameSession = gameSession;
        this.splashScreenFinished = false;
        this.currentScreen = Screens.SPLASH;
        switchToScreen(Screens.SPLASH);
        setTimerOnSplashScreen();
    }

    /***
     * This method uses an anonymous inner class, and it is used to
     * ensure that the splashScreen only gets displayed for a certain
     * amount of seconds (delay seconds).
     */
    private void setTimerOnSplashScreen() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                splashScreenFinished = true;
                switchToScreen(Screens.MENU);
            }
        }, 3); // seconds delay
    }

    /***
     * This method gets called when a key was released by the player.
     * It is used to check the current screen and switch screens based on the players input.
     * @param keycode one of the constants in {@link com.badlogic.gdx.Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        if (currentScreen == Screens.SPLASH && !splashScreenFinished) {
            splashScreenFinished = true;
            Timer.instance().clear(); // Clear the timer
            switchToScreen(Screens.MENU);
        } else if (currentScreen == Screens.MENU && keycode == Keys.SPACE) {
            // Switch to PlayScreen and pass the existing ScoreManager
            switchToScreen(Screens.PLAY);
        } else if (currentScreen == Screens.PLAY && keycode == Keys.ESCAPE) {
            switchToScreen(Screens.MENU);
        }
        return true;
    }

    /***s
     * This method is a helper method to the keyUp method.
     * @param screen to switch to
     */
    private void switchToScreen(Screens screen) {
        currentScreen = screen;
        gameSession.setScreen(screen.getScreenInstance());
    }

}
