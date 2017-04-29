package com.mygdx.game.screens;

/**
 * Created by Michael on 2017/4/16.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.AttackOnGyro;

public class MainGameScreen implements Screen{

    final AttackOnGyro game;
    private GameStage stage;

    public MainGameScreen(final AttackOnGyro game){
        this.game = game;
        //http://stackoverflow.com/questions/16347297/difference-between-libgdx-stage-and-screen
        stage = new GameStage(game);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
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
