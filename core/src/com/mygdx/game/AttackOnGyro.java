package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.async.AsyncExecutor;

import com.mygdx.game.screens.StartScreen;
import com.mygdx.game.screens.MainGameScreen;


public class AttackOnGyro extends Game {

	public MainGameScreen gameScreen;
	public StartScreen startScreen;
	public SpriteBatch batch;
	public BitmapFont font;
    public AsyncExecutor executor;
    public Animation<TextureRegion> loadingAnimation;





    @Override
	public void create () {

		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("style/StyleFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 50;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
		generator.dispose();

        executor = new AsyncExecutor(2);
        loadingAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("img/loading.gif").read());

		startScreen = new com.mygdx.game.screens.StartScreen(this);
        setScreen(startScreen);
	}



	public void render(){
		super.render();
	}

	public void dispose(){
		font.dispose();
		batch.dispose();
	}

}
