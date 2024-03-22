package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import se.yrgo.jumpybirb.JumpyBirb;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.MenuListener;
import se.yrgo.jumpybirb.utils.ScreenSwitcher;
import se.yrgo.jumpybirb.utils.Screens;

import static com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;

/***
 * The menu screen that always shows after the splash screen when you run the game.
 * Choose to play again, show highscore from current game session, enable/disable music.
 * The game also returns to this screen after the game over screen gets displayed.
 */
public class MenuScreen implements Screen, MenuListener {
    public static final String TAG = MenuScreen.class.getName();
    SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture normalButtonTexture;
    private Texture hardButtonTexture;
    private Texture highscoreButtonTexture;
    private Texture exitButtonTexture;
    private Texture normalButtonSelectedTexture;
    private Texture hardButtonSelectedTexture;
    private Texture highscoreButtonSelectedTexture;
    private Texture exitButtonSelectedTexture;
    private ImageButton playNormalButton;
    private ImageButton playHardButton;
    private ImageButton exitButton;
    private boolean buttonStylesInitialized = false;
    private int currentSelectedButtonIndex = 0;
    private final ScreenSwitcher screenSwitcher;
    private InputHandler inputHandler;
    private Stage stage;

    public MenuScreen(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        screenSwitcher = JumpyBirb.getScreenSwitcher();
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /***
     * This method is called when this screen becomes
     * the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.app.log(TAG, "show() called");

        // Textures for normal buttons
        normalButtonTexture = new Texture(Gdx.files.internal("NormalButton.png"));
        hardButtonTexture = new Texture(Gdx.files.internal("HardButton.png"));
        highscoreButtonTexture = new Texture(Gdx.files.internal("Highscore.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("ExitButton.png"));


        // Texture for selected buttons
        normalButtonSelectedTexture = new Texture(Gdx.files.internal("NormalButton-checked.png"));
        hardButtonSelectedTexture = new Texture(Gdx.files.internal("HardButton-checked.png"));
        highscoreButtonSelectedTexture = new Texture(Gdx.files.internal("Highscore-checked.png"));
        exitButtonSelectedTexture = new Texture(Gdx.files.internal("ExitButton-checked.png"));

        // Initialize button styles and instances
        initializeButtonsAndStyles();

        // Create the stage for allowing buttons to be clickable with ClickListeners
        stage = new Stage(new ScreenViewport());

        // Create a table to hold the buttons
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.center().bottom().padBottom(Gdx.graphics.getHeight() * 0.2f); // Adjust Y position here
        stage.addActor(buttonTable);

        // Add the buttons to the table with padding
        float padding = 20f; // Adjust padding between buttons
        buttonTable.add(playNormalButton).padBottom(padding).row();
        buttonTable.add(playHardButton).padBottom(padding).row();
        buttonTable.add(exitButton).padBottom(padding).row();

        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("Welcome1.jpg"));

        // Get the input handling working correctly with multiplexer
        configureInputMultiplexer();

        // Add click listeners to the buttons
        addClickListenersToMenuButtons();
    }

    /**
     * This method is used to load the textures and create
     * the menu buttons and the style they get when selected.
     */
    private void initializeButtonsAndStyles() {
        if (!buttonStylesInitialized) {
            ImageButtonStyle playNormalButtonStyle = new ImageButtonStyle();
            playNormalButtonStyle.up = new TextureRegionDrawable(normalButtonTexture);
            playNormalButtonStyle.checked = new TextureRegionDrawable(normalButtonSelectedTexture);

            ImageButtonStyle playHardButtonStyle = new ImageButtonStyle();
            playHardButtonStyle.up = new TextureRegionDrawable(hardButtonTexture);
            playHardButtonStyle.checked = new TextureRegionDrawable(hardButtonSelectedTexture);

            ImageButtonStyle exitButtonStyle = new ImageButtonStyle();
            exitButtonStyle.up = new TextureRegionDrawable(exitButtonTexture);
            exitButtonStyle.checked = new TextureRegionDrawable(exitButtonSelectedTexture);

            playNormalButton = new ImageButton(playNormalButtonStyle);
            playHardButton = new ImageButton(playHardButtonStyle);
            exitButton = new ImageButton(exitButtonStyle);

            buttonStylesInitialized = true;
        }
    }

    /**
     * This method is used to get the input handling of
     * the stage in MenuScreen to work properly with the ClickListeners.
     * Without this configuration, the player is unable to click the menu buttons.
     */
    private void configureInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * This method is used to allow buttons to be clickable.
     * Here we can set the action we want for each button in the menu.
     */
    private void addClickListenersToMenuButtons() {
        playNormalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(TAG, "Play Normal Button clicked");
                normalButtonClicked();
                Gdx.input.setInputProcessor(inputHandler);
            }
        });

        playHardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(TAG, "Play Hard Button clicked");
                hardButtonClicked();
                Gdx.input.setInputProcessor(inputHandler);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(TAG, "Exit Button clicked");
                Gdx.app.exit();
            }
        });
    }

    /***
     * This method is called when the Application should render itself.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        currentSelectedButtonIndex = inputHandler.getSelectedButtonIndexMainMenu();
        updateButtonStyles();

        // Draw the stage
        stage.act(delta);
        stage.draw();
    }

    /**
     * This method is used to update the currently selected button
     * if the player traverses the menu with keyboard.
     * Texture for selected button is an image file instantiated
     * in the show method.
     */
    private void updateButtonStyles() {
        // Update button styles only when the selected button changes
        switch (currentSelectedButtonIndex) {
            case 0:
                playNormalButton.setChecked(true);
                playHardButton.setChecked(false);
                exitButton.setChecked(false);
                break;
            case 1:
                playNormalButton.setChecked(false);
                playHardButton.setChecked(true);
                exitButton.setChecked(false);
                break;
            case 2:
                playNormalButton.setChecked(false);
                playHardButton.setChecked(false);
                exitButton.setChecked(true);
                break;
            default:
                break;
        }
    }

    /**
     * Action to be done if the "NORMAL" button is clicked.
     */
    @Override
    public void normalButtonClicked() {
        screenSwitcher.switchToScreen(Screens.PLAY);
        Gdx.app.log(TAG, "playButtonClicked() called");
    }

    /**
     * Action to be done if the "HARD" button is clicked.
     */
    @Override
    public void hardButtonClicked() {
        screenSwitcher.switchToScreen(Screens.PLAY);
        Gdx.app.log(TAG, "hardButtonClicked() called");
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
        stage.dispose();
        normalButtonTexture.dispose();
        normalButtonSelectedTexture.dispose();
        hardButtonTexture.dispose();
        hardButtonSelectedTexture.dispose();
        exitButtonTexture.dispose();
        exitButtonSelectedTexture.dispose();
        backgroundTexture.dispose();
    }
}
