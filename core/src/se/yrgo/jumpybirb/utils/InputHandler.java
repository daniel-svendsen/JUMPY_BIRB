package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.screens.PlayScreen;

public class InputHandler extends InputAdapter {
    public static final String TAG = InputHandler.class.getSimpleName();
    private final ScreenSwitcher screenSwitcher;
    private final JumpyBirb gameSession;

    public InputHandler(JumpyBirb gameSession, ScreenSwitcher screenSwitcher) {
        this.gameSession = gameSession;
        this.screenSwitcher = screenSwitcher;
    }

    @Override
    public boolean keyDown(int keycode) {
        Screens currentScreen = screenSwitcher.getCurrentScreen();
        Gdx.app.log(TAG, "Current screen: " + currentScreen);

        // splash screen
        if (currentScreen == Screens.SPLASH) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "keyDown called: switch to MenuScreen");
            return true;
        } else if (currentScreen == Screens.MENU) {
            if (keycode == Input.Keys.SPACE) {
                screenSwitcher.switchToScreen(Screens.PLAY);
                Gdx.app.log(TAG, "keyDown called: switch to PlayScreen");
                return true;
            }
        } else if (currentScreen == Screens.PLAY) {
            PlayScreen playScreen = (PlayScreen) gameSession.getScreen();
            PlayScreen.GameState currentGameState = playScreen.getCurrentGameState();

            switch (currentGameState) {
                case READY:
                    handleReadyState(keycode, currentScreen, playScreen);
                    break;
                case RUNNING:
                    handleRunningState(keycode, currentScreen, playScreen);
                    break;
                case GAME_OVER:
                    handleGameOverState(keycode, currentScreen, playScreen);
                    break;
                default:
                    break;
            }

            if (keycode == Input.Keys.SPACE) {
                birbJump(playScreen);
                Gdx.app.log(TAG, "keyDown called: handlePlayer()");
                return true;
            }

            if (keycode == Input.Keys.ESCAPE) {
                screenSwitcher.switchToScreen(Screens.MENU);
                Gdx.app.log(TAG, "keyDown called: escape to switch to MenuScreen");
                return true;
            }
        }

        return false;
    }


    private void handleReadyState(int keycode, Screens currentScreen, PlayScreen playScreen) {
        if (currentScreen == Screens.PLAY && keycode == Input.Keys.SPACE) {
            // Switch to RUNNING state
            screenSwitcher.switchToScreen(Screens.PLAY);
            Gdx.app.log(TAG, "keyDown called: switch to RunningState");
        }
    }

    private void handleRunningState(int keycode, Screens currentScreen, PlayScreen playScreen) {
        if (currentScreen == Screens.PLAY) {
            if (keycode == Input.Keys.SPACE) {
                birbJump(playScreen);
                Gdx.app.log(TAG, "handleRunningState() made birb jump");
            } else if (keycode == Input.Keys.ESCAPE) {
                screenSwitcher.switchToScreen(Screens.MENU);
                Gdx.app.log(TAG, "handleRunningState(): pressed escape to switch to MenuScreen");
            }
        }
    }

    private void handleGameOverState(int keycode, Screens currentScreen, PlayScreen playScreen) {
        if (currentScreen == Screens.PLAY && keycode == Input.Keys.SPACE) {
            // Restart the game or do other actions
            screenSwitcher.switchToScreen(Screens.PLAY);
            Gdx.app.log(TAG, "handleGameOverState(): restarted the game");
        }
    }

    private void birbJump(PlayScreen playScreen) {
        playScreen.getBirb().jump();
        Gdx.app.log(TAG, "birbJump called");
    }
}
