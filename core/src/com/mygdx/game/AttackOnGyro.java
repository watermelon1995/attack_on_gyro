package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MainGameScreen;

public class AttackOnGyro extends Game {

	public MainGameScreen gameScreen;
	public com.mygdx.game.screens.StartScreen startScreen;
	public SpriteBatch batch;
	public BitmapFont font;


	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().scale(2f);
		startScreen = new com.mygdx.game.screens.StartScreen(this);
        setScreen(startScreen);
	}

	public void render(){
		super.render();
	}

	public void dispose(){
		batch.dispose();
		font.dispose();
	}

}
