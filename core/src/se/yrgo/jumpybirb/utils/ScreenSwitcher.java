package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.utils.Timer;
import se.yrgo.jumpybirb.JumpyBirb;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import se.yrgo.jumpybirb.screens.*;

public class ScreenSwitcher extends InputAdapter {
    private final Game game;
    private Screens currentScreen;
    private boolean splashScreenFinished;

    public ScreenSwitcher(JumpyBirb game) {
        this.game = game;
        this.splashScreenFinished = false;

        setTimerOnSplashScreen();
    }

    /***
     * Timer with an anonymous inner class
     */
    private void setTimerOnSplashScreen() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                splashScreenFinished = true;
                switchToScreen(Screens.MENU);            }
        }, 3); // seconds delay
    }

    @Override
    public boolean keyUp(int keycode) {
        if (currentScreen == Screens.SPLASH && !splashScreenFinished) {
            splashScreenFinished = true;
            Timer.instance().clear(); // Clear the timer
            switchToScreen(Screens.MENU);
        } else if (currentScreen == Screens.MENU && keycode == Keys.ENTER) {
            switchToScreen(Screens.PLAY);
        } else if (currentScreen == Screens.PLAY && keycode == Keys.ESCAPE) {
            switchToScreen(Screens.MENU);
        }
        return true;
    }

    private void switchToScreen(Screens screen) {
        currentScreen = screen;
        game.setScreen(screen.getScreenInstance());
    }

}
