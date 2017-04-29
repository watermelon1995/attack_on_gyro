package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.AttackOnGyro;


/**
 * Created by kin on 4/18/17.
 */

public class GameOverScreen implements Screen {
    final AttackOnGyro game;
    OrthographicCamera camera;
    boolean isWin;
    Texture texture;
    Sprite sprite;
    final String winText = "You Win!!";
    final String loseText = "You Lose!!";


    public GameOverScreen(final AttackOnGyro game, boolean isWin){
        this.game = game;
        this.isWin = isWin;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (isWin){
            texture = new Texture(Gdx.files.internal("img/xd.png"));
        }else{
            texture = new Texture(Gdx.files.internal("img/no_eye_see.png"));
        }
        sprite = new Sprite(texture);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        if (isWin){
            game.font.draw(game.batch, winText, Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2);
        }else{
            game.font.draw(game.batch, loseText, Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2);
        }
        sprite.draw(game.batch);
        game.batch.end();


        if (Gdx.input.isTouched()) {
            game.setScreen(new MainGameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
