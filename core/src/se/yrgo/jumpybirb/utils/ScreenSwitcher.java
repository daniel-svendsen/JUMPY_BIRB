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
    private final SplashScreen splashScreen;
    private final MenuScreen menuScreen;
    private final PlayScreen playScreen;
    private final GameOverScreen gameOverScreen;
    private final HighScoreScreen highScoreScreen;
    private int currentScreen;
    private boolean splashScreenFinished;

    public ScreenSwitcher(JumpyBirb game) {
        this.game = game;
        splashScreen = new SplashScreen();
        menuScreen = new MenuScreen();
        playScreen = new PlayScreen();
        gameOverScreen = new GameOverScreen();
        highScoreScreen = new HighScoreScreen();
        currentScreen = 1;
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
                switchToMenuScreen();
            }
        }, 3); // seconds delay
    }

    @Override
    public boolean keyUp(int keycode) {
        if (currentScreen == 1 && !splashScreenFinished) {
            splashScreenFinished = true;
            Timer.instance().clear(); // Clear the timer
            switchToMenuScreen();
        } else if (currentScreen == 2 && keycode == Keys.ENTER) {
            switchToPlayScreen();
        } else if (currentScreen == 3 && keycode == Keys.ESCAPE) {
            switchToMenuScreen();
        }
        return true;
    }

    private void switchToMenuScreen() {
        currentScreen = 2;
        game.setScreen(menuScreen);
    }

    private void switchToPlayScreen() {
        currentScreen = 3;
        game.setScreen(playScreen);
    }


    private void switchToGameOverScreen() {
        currentScreen = 3;
        game.setScreen(gameOverScreen);
    }

    private void switchToHighScoreScreen() {
        currentScreen = 3;
        game.setScreen(highScoreScreen);
    }

}
