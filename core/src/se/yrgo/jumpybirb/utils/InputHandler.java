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

    private void handleSplashScreen(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "keyDown called: switch to MenuScreen");
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        var currentScreen = screenSwitcher.getCurrentScreen();
        if (currentScreen == Screens.SPLASH) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "TOUCH DOWN");
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void handleMenuScreen(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            screenSwitcher.switchToScreen(Screens.PLAY);
            Gdx.app.log(TAG, "keyDown called: switch to PlayScreen");
        }
    }

    private void handlePlayScreen(int keycode) {
        PlayScreen playScreen = (PlayScreen) gameSession.getScreen();
        PlayScreen.GameState currentGameState = playScreen.getCurrentGameState();

        switch (currentGameState) {
            case MENU:
                //TODO implement menu logic
                break;
            case READY:
                handleReadyState(keycode, playScreen);
                break;
            case RUNNING:
                handleRunningState(keycode, playScreen);
                break;
            case GAME_OVER:
                handleGameOverState(keycode, playScreen);
                break;
        }
    }

    private void birbJump(PlayScreen playScreen) {
        playScreen.getPlayerBirb().jump();
        Gdx.app.log(TAG, "birbJump called");
    }

    private void handleReadyState(int keycode, PlayScreen playScreen) {
        if (keycode == Input.Keys.SPACE) {
            // transition to RUNNING state using setter method
            playScreen.setCurrentGameState(PlayScreen.GameState.RUNNING);
            Gdx.app.log(TAG, "handleReadyState() called: switch to GameState.PLAY");
        }
    }

    private void handleRunningState(int keycode, PlayScreen playScreen) {
        if (keycode == Input.Keys.SPACE) {
            birbJump(playScreen);
            Gdx.app.log(TAG, "handleRunningState() made birb jump");
        } else if (keycode == Input.Keys.ESCAPE) {
            // Exit the game
            Gdx.app.exit();
            Gdx.app.log(TAG, "handleRunningState(): exited the game");
        }
    }

    private void handleGameOverState(int keycode, PlayScreen playScreen) {
        if (keycode == Input.Keys.SPACE) {
            screenSwitcher.switchToScreen(Screens.PLAY);
            Gdx.app.log(TAG, "handleGameOverState(): restarted the game");
        }
    }

    private void handleHighScoreScreen(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ESCAPE) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "handleHighScoreScreen() called: switch to PlayScreen");
        }
    }

    private void handleGameOverScreen(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ESCAPE) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "handleGameOverScreen() called: switch to PlayScreen");
        }
    }
}
