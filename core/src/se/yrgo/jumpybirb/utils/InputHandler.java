package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.screens.PlayScreen;
import se.yrgo.jumpybirb.sprites.Birb;

public class InputHandler extends InputAdapter {
    public static final String TAG = InputHandler.class.getSimpleName();
    private ScreenSwitcher screenSwitcher;
    private final JumpyBirb gameSession;

    public InputHandler(JumpyBirb gameSession, ScreenSwitcher screenSwitcher) {
        this.gameSession = gameSession;
        this.screenSwitcher = screenSwitcher;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (screenSwitcher.getCurrentScreen() == Screens.SPLASH) {
            screenSwitcher.switchToScreen(Screens.MENU);
            Gdx.app.log(TAG, "keyDown called: switch to MenuScreen");
            return true;
        } else if (screenSwitcher.getCurrentScreen() == Screens.MENU) {
            if (keycode == Input.Keys.SPACE) {
                screenSwitcher.switchToScreen(Screens.PLAY);
                Gdx.app.log(TAG, "keyDown called: switch to PlayScreen");
                return true;
            }
        } else if (screenSwitcher.getCurrentScreen() == Screens.PLAY) {
            if (keycode == Input.Keys.SPACE) {
                birbJump();
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

    private void birbJump() {
        PlayScreen playScreen = (PlayScreen) gameSession.getScreen();
        playScreen.getBirb().jump();
        Gdx.app.log(TAG, "birbJump called");
    }
}
