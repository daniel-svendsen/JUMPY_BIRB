package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import se.yrgo.jumpybirb.sprites.Birb;

public class InputHandler extends InputAdapter {
    private Birb birb;

    public MyInputProcessor(Birb birb) {
        this.birb = birb;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            birb.jump();
            return true; // Return true to indicate that the input has been processed
        }
        return false; // Return false if the input was not processed
    }
}
