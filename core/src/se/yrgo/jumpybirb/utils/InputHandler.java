package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.screens.HighScoreScreen;
import se.yrgo.jumpybirb.screens.PlayScreen;
import se.yrgo.jumpybirb.screens.SplashScreen;

public class InputHandler extends InputAdapter {
    private static final String TAG = InputHandler.class.getSimpleName();
    private final ScreenSwitcher screenSwitcher;
    private final JumpyBirb gameSession;
    private MenuListener menuListener;
    private Stage stage;
    private int selectedButtonIndexMainMenu = 0;
    private int selectedButtonIndexGameOver = 0;

    public InputHandler(JumpyBirb gameSession, ScreenSwitcher screenSwitcher, MenuListener menuListener) {
        this.gameSession = gameSession;
        this.screenSwitcher = screenSwitcher;
        this.menuListener = menuListener;
    }

    public void setMenuListener(MenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
            SplashScreen.setPlayScreenDisplayed(true);
            Gdx.app.log(TAG, "Switched to MenuScreen");
        } else if (currentScreen == Screens.MENU) {
            Gdx.app.log(TAG, "Touch down event received at coordinates: (" + screenX + ", " + screenY + ")");
        } else if (currentScreen == Screens.PLAY) {
            PlayScreen playScreen = (PlayScreen) gameSession.getScreen();
            PlayScreen.GameState currentGameState = playScreen.getCurrentGameState();
            if (button == Input.Buttons.LEFT && currentGameState != PlayScreen.GameState.READY) {
                makeBirbJump(playScreen);
                Gdx.app.log(TAG, "Running state: Birb jumped");
            }
        }
        return false;
    }

    private void handleSplashScreen(int keycode) {
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
            switchToMenuScreen();
            SplashScreen.setPlayScreenDisplayed(true);
        }
    }

    private void handleMenuScreen(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                navigateMenu(-1); // Move selection up
                break;
            case Input.Keys.DOWN:
                navigateMenu(1); // Move selection down
                break;
            case Input.Keys.ENTER, Input.Keys.SPACE:
                triggerSelectedMenuButtonAction(); // Trigger action for selected button
                break;
            default:
                break;
        }
    }

    private void navigateMenu(int direction) {
        selectedButtonIndexMainMenu += direction;

        if (selectedButtonIndexMainMenu < 0 || selectedButtonIndexMainMenu > 2) {
            selectedButtonIndexMainMenu = 0;
        }
        Gdx.app.log(TAG, "Selected button index: " + selectedButtonIndexMainMenu);
    }

    public int getSelectedButtonIndexMainMenu() {
        return selectedButtonIndexMainMenu;
    }

    /**
     * Determine which menu button is currently selected and trigger its action
     */
    private void triggerSelectedMenuButtonAction() {
        switch (selectedButtonIndexMainMenu) {
            case 0:
                screenSwitcher.switchToScreen(Screens.PLAY);
                selectedButtonIndexMainMenu = 0;
                break;
            case 1:
                screenSwitcher.switchToScreen(Screens.HIGH_SCORE);
                selectedButtonIndexMainMenu = 0;
                break;
            case 2:
                Gdx.app.exit();
                break;
        }
    }

    private void handlePlayScreen(int keycode) {
        PlayScreen playScreen = (PlayScreen) gameSession.getScreen();
        PlayScreen.GameState currentGameState = playScreen.getCurrentGameState();

        switch (currentGameState) {
            case READY:
                if (keycode == Input.Keys.ESCAPE) {
                    switchToMenuScreen();
                    Gdx.app.log(TAG, "Ready state: Switched to Menu Screen");
                } else if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
                    playScreen.setCurrentGameState(PlayScreen.GameState.RUNNING);
                }
                break;
            case RUNNING:
                if (keycode == Input.Keys.SPACE) {
                    makeBirbJump(playScreen);
                    Gdx.app.log(TAG, "Running state: Birb jumped");
                } else if (keycode == Input.Keys.ESCAPE) {
                    screenSwitcher.switchToScreen(Screens.MENU);
                    playScreen.setCurrentGameState(PlayScreen.GameState.READY);
                }
                break;
        }
    }

    private void handleGameOverScreen(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                navigateGameOverMenu(-1); // Move selection up
                break;
            case Input.Keys.DOWN:
                navigateGameOverMenu(1); // Move selection down
                break;
            case Input.Keys.SPACE, Input.Keys.ENTER:
                triggerSelectedGameOverButtonAction();
                break;
            default:
                break;
        }
    }

    private void navigateGameOverMenu(int direction) {
        selectedButtonIndexGameOver += direction;

        if (selectedButtonIndexGameOver < 0 || selectedButtonIndexGameOver > 1) {
            selectedButtonIndexGameOver = 0;
        }
        Gdx.app.log(TAG, "Selected button index: " + selectedButtonIndexGameOver);
    }

    public int getSelectedButtonIndexGameOver() {
        return selectedButtonIndexGameOver;
    }

    /**
     * Determine which menu button is currently selected and trigger its action
     */
    private void triggerSelectedGameOverButtonAction() {
        switch (selectedButtonIndexGameOver) {
            case 0:
                screenSwitcher.switchToScreen(Screens.PLAY);
                selectedButtonIndexMainMenu = 0;
                break;
            case 1:
                Gdx.app.exit();
                break;
        }
    }

    private void handleHighScoreScreen(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
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
}
