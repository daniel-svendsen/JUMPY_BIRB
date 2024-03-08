package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.screens.PlayScreen;

public class InputHandler extends InputAdapter {
    private static final String TAG = InputHandler.class.getSimpleName();

    private final ScreenSwitcher screenSwitcher;
    private final JumpyBirb gameSession;
private boolean playAgainSelected = true;
    public InputHandler(JumpyBirb gameSession, ScreenSwitcher screenSwitcher) {
        this.gameSession = gameSession;
        this.screenSwitcher = screenSwitcher;
    }

    @Override
    public boolean keyDown(int keycode) {
        Screens currentScreen = screenSwitcher.getCurrentScreen();
        Gdx.app.log(TAG, "Current screen: " + currentScreen);

        switch (currentScreen) {
            case SPLASH:
                handleSplashScreen(keycode);
                break;
            case MENU:
                handleMenuScreen(keycode);
                break;
            case PLAY:
                handlePlayScreen(keycode);
                break;
            case GAME_OVER:
                handleGameOverScreen(keycode);
                break;
            case HIGH_SCORE:
                handleHighScoreScreen(keycode);
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Screens currentScreen = screenSwitcher.getCurrentScreen();
        if (currentScreen == Screens.SPLASH) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "TOUCH DOWN: Switched to MenuScreen");
        }
        return false;
    }

    private void handleSplashScreen(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            switchToMenuScreen();
        }
    }

    private void handleMenuScreen(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            switchToPlayScreen();
        }
    }

    private void handlePlayScreen(int keycode) {
        PlayScreen playScreen = (PlayScreen) gameSession.getScreen();
        PlayScreen.GameState currentGameState = playScreen.getCurrentGameState();

        switch (currentGameState) {
            case READY:
                if (keycode == Input.Keys.SPACE) {
                    playScreen.setCurrentGameState(PlayScreen.GameState.RUNNING);
                    Gdx.app.log(TAG, "Ready state: Switched to Running state");
                }
                break;
            case RUNNING:
                if (keycode == Input.Keys.SPACE) {
                    makeBirbJump(playScreen);
                    Gdx.app.log(TAG, "Running state: Birb jumped");
                } else if (keycode == Input.Keys.ESCAPE) {
                    exitGame();
                }
                break;
        }
    }

    private void handleGameOverScreen(int keycode) {
        // Handle input
        if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
            playAgainSelected = !playAgainSelected; // Toggle selection between "Play Again" and "Exit"
        }
        if (keycode == Input.Keys.ENTER) {
            if (playAgainSelected) {
                // Start a new game
                screenSwitcher.switchToScreen(Screens.PLAY); // Assuming PLAY is your play screen identifier
            }
            else {
                // Exit the game
                Gdx.app.exit();
            }
        }
        else if (keycode == Input.Keys.SPACE) {
            if (playAgainSelected) {
                // Start a new game
                screenSwitcher.switchToScreen(Screens.PLAY); // Assuming PLAY is your play screen identifier
            }
            else {
                // Exit the game
                Gdx.app.exit();
            }
        }
    }

    private void handleHighScoreScreen(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ESCAPE) {
            switchToMenuScreen();
        }
    }

    private void switchToMenuScreen() {
        screenSwitcher.switchToScreen(Screens.MENU);
        Gdx.app.log(TAG, "Switched to MenuScreen");
    }

    private void switchToPlayScreen() {
        screenSwitcher.switchToScreen(Screens.PLAY);
        Gdx.app.log(TAG, "Switched to PlayScreen");
    }

    private void makeBirbJump(PlayScreen playScreen) {
        playScreen.getPlayerBirb().jump();
        Gdx.app.log(TAG, "Birb jumped");
    }

    private void exitGame() {
        Gdx.app.exit();
        Gdx.app.log(TAG, "Exited the game");
    }
}
