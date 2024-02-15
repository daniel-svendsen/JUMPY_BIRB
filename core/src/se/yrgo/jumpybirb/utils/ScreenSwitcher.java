package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Timer;
import se.yrgo.jumpybirb.JumpyBirb;

public class ScreenSwitcher extends InputAdapter {
    private final JumpyBirb gameSession;
    private Screens currentScreen;
    private boolean splashScreenFinished;

    public ScreenSwitcher(JumpyBirb gameSession) {
        this.gameSession = gameSession;
        this.splashScreenFinished = false;
        this.currentScreen = Screens.SPLASH;
        switchToScreen(Screens.SPLASH);
        setTimerOnSplashScreen();
    }

    private void setTimerOnSplashScreen() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                splashScreenFinished = true;
                switchToScreen(Screens.MENU);
            }
        }, 3); // seconds delay
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (currentScreen) {
            case SPLASH:
                if (!splashScreenFinished) {
                    splashScreenFinished = true;
                    Timer.instance().clear();
                    switchToScreen(Screens.MENU);
                }
                break;
            case MENU:
                if (keycode == Keys.SPACE) {
                    switchToScreen(Screens.PLAY);
                }
                break;
            case PLAY:
                if (keycode == Keys.ESCAPE) {
                    switchToScreen(Screens.MENU);
                }
                break;
        }
        return true;
    }

    private void switchToScreen(Screens screen) {
        currentScreen = screen;
        gameSession.setScreen(screen.getScreenInstance());
    }
}