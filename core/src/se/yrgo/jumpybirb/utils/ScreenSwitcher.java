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
    private final JumpyBirb gameSession;
    private Screens currentScreen;
    private PlayScreen playScreen;

    /***
     * Constructor
     * @param gameSession the current game session
     */
    public ScreenSwitcher(JumpyBirb gameSession) {
        this.gameSession = gameSession;
        this.currentScreen = Screens.SPLASH;
        //playScreen = new PlayScreen(JumpyBirb.getScreenSwitcher());
        this.playScreen = new PlayScreen(this);
    }

    public Screens getCurrentScreen() {
        return currentScreen;
    }

    public PlayScreen getPlayScreen() {
        return playScreen;
    }

    public void switchToScreen(Screens screen) {
        if (currentScreen != screen) {
            currentScreen = screen;
            switch (screen) {
                case PLAY:
                    if (playScreen.getCurrentGameState() == PlayScreen.GameState.RUNNING) {
                        playScreen.resetGame(); // Reset the game state if it's already running
                    } else {
                        playScreen = new PlayScreen(this); // Create a new instance if not already created
                    }
                    gameSession.setScreen(playScreen);
                    break;
                case MENU:
                    if (playScreen.getCurrentGameState() == PlayScreen.GameState.RUNNING) {
                        playScreen.resetGame();
                    }
                    gameSession.setScreen(screen.getScreenInstance());
                    break;
                default:
                    gameSession.setScreen(screen.getScreenInstance());
                    break;
            }
            Gdx.app.log(TAG, " switched to " + screen);
        }
    }
}

