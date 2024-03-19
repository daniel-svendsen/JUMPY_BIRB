package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.MenuListener;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;
import se.yrgo.jumpybirb.utils.Screens;

/***
 * The menu screen that always shows after the splash screen when you run the game.
 * Choose to play again, show highscore from current game session, enable/disable music.
 * The game also returns to this screen after the game over screen gets displayed.
 */
public class MenuScreen implements Screen, MenuListener {
    public static final String TAG = MenuScreen.class.getName();
    private static final Float HEADER_FONT_SCALE = 3.0f;
    private static final Float TEXT_FONT_SCALE = 2.0f;

    BitmapFont headerFont;
    BitmapFont textFont;
    SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture normalButtonTexture;
    private Texture hardButtonTexture;
    private Texture exitButtonTexture;
    private final ScreenSwitcher screenSwitcher;
    private final InputHandler inputHandler;
    private Stage stage;

    public MenuScreen(InputHandler inputHandler) {
        this.inputHandler = JumpyBirb.getInputHandler();
        screenSwitcher = JumpyBirb.getScreenSwitcher();
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");
        stage = new Stage()
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("Welcome1.jpg"));

        normalButtonTexture = new Texture(Gdx.files.internal("NormalButton.png"));
        hardButtonTexture = new Texture(Gdx.files.internal("HardButton.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("ExitButton.png"));

        headerFont = new BitmapFont();
        headerFont.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        headerFont.getData().setScale(HEADER_FONT_SCALE);

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        textFont.getData().setScale(TEXT_FONT_SCALE);

        Gdx.input.setInputProcessor(inputHandler);
    }

    /***
     * This method is called when the Application should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw text "Menu" and background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        headerFont.draw(batch, "Menu", Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f, 0, Align.left, false);

        float textPadding = 50f;
        textFont.draw(batch, "Press SPACE to play game", Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f - textPadding, 0, Align.left, false);
        batch.end();
    }

    @Override
    public void playButtonClicked() {
        screenSwitcher.switchToScreen(Screens.PLAY);
    }

    @Override
    public void menuButtonClicked() {
        screenSwitcher.switchToScreen(Screens.PLAY);
    }

    /***
     * This method is called when the Application is resized,
     * which can happen at any point during a non-paused state.
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "resize(" + width + ", " + height + ") called");
    }

    /***
     * This method is called when the Application is paused,
     * usually when it's not active or visible on-screen.
     */
    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause() called");
    }

    /***
     * This method is called when the Application is resumed from
     * a paused state, usually when it regains focus.
     */
    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume() called");
    }

    /***
     * This method is called when this screen is no longer
     * the current screen for the game.
     */
    @Override
    public void hide() {
        Gdx.app.log(TAG, "hide() called");
    }

    /***
     * This method is called when this screen should
     * release all resources. Preceded by a call to pause().
     */
    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose() called");
        batch.dispose();
        headerFont.dispose();
        textFont.dispose();
        backgroundTexture.dispose();
    }
}
