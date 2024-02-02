package se.yrgo.jumpybirb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class JumpyBirb extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Player player;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		player = new Player();
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		// Update player logic
		player.update(delta);
		ScreenUtils.clear(1, 0, 0, 1);
		// Render the player
		batch.begin();
		player.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		
	}
}
