package se.yrgo.jumpybirb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import se.yrgo.jumpybirb.sprites.Birb;
import se.yrgo.jumpybirb.utils.InputHandler;
import se.yrgo.jumpybirb.utils.ScoreManager;

public class PlayScreen implements Screen {
    private SpriteBatch batch;
    private Birb birb;
    private ScoreManager scoreManager;
    private Texture backgroundTexture;
    private BitmapFont textFont;
    private static final float TEXT_FONT_SCALE = 2.0f;

    public PlayScreen() {
        scoreManager = ScoreManager.getInstance();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("Bakgrund1.jpg");
        birb = new Birb();
        Gdx.input.setInputProcessor(new InputHandler(birb));

        textFont = new BitmapFont();
        textFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textFont.getData().setScale(TEXT_FONT_SCALE);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        birb.update(delta);

        batch.begin();
        drawText();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(birb.getTexture(), birb.getPosition().x, birb.getPosition().y);
        batch.end();

        drawScores();
    }

    private void drawText() {
        float textPadding = 50f;
        textFont.draw(batch, "Press Esc to go to Menu", Gdx.graphics.getWidth() / 4f,
                Gdx.graphics.getHeight() / 3f - textPadding, 0, Align.left, false);
    }

    private void drawScores() {
        int currentScore = scoreManager.getScore();
        int highScore = scoreManager.getHighScore();

        batch.begin();
        textFont.draw(batch, "Score: " + currentScore, 10, Gdx.graphics.getHeight() - 10f);
        textFont.draw(batch, "High Score: " + highScore, 10, Gdx.graphics.getHeight() - 10f - 50f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}