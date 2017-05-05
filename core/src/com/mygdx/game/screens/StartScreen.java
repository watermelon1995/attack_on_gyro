package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.game.AttackOnGyro;

/**
 * Created by kin on 4/18/17.
 */

public class StartScreen implements Screen{

    OrthographicCamera camera;
    final AttackOnGyro game;

    //Stage used to hold the button
    private Stage startScreen_Stage;

    private FillViewport viewport;

    Texture imgTexture;
    Image titleImage;
    TextButton singleStart;
    TextButton multiStart;

    final String singleStart_Text = "Single Player";
    final String multiStart_Text = "Multi Player";


    public StartScreen(final AttackOnGyro game){
        this.game = game;


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        startScreen_Stage = new Stage(viewport);


        imgTexture = new Texture(Gdx.files.internal("img/title.png"));

        titleImage = new Image(imgTexture);
        titleImage.scaleBy(1.2f);
        titleImage.setPosition(Gdx.graphics.getWidth()/2 - titleImage.getWidth(), Gdx.graphics.getHeight()/2 - 50);

        startScreen_Stage.addActor(titleImage);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;

        singleStart = new TextButton(singleStart_Text, textButtonStyle);
        singleStart.setPosition(Gdx.graphics.getWidth()/2 - singleStart.getWidth()/2, Gdx.graphics.getHeight()/2 - 200);

        startScreen_Stage.addActor(singleStart);

        multiStart = new TextButton(multiStart_Text, textButtonStyle);
        multiStart.setPosition(Gdx.graphics.getWidth()/2 - multiStart.getWidth()/2, Gdx.graphics.getHeight()/2 - 400);
        startScreen_Stage.addActor(multiStart);
        Gdx.input.setInputProcessor(startScreen_Stage);

        singleStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainGameScreen(game));
                dispose();
            }
        });


        multiStart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });




    }

    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        startScreen_Stage.act(delta);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
//        game.batch.draw(imgTexture, Gdx.graphics.getWidth()/2 - imgTexture.getWidth(),Gdx.graphics.getHeight()/2 , imgTexture.getWidth()*2, imgTexture.getHeight()*2);
        startScreen_Stage.draw();
        game.batch.end();



//        if (Gdx.input.isTouched()) {
//            game.setScreen(new MainGameScreen(game));
//            dispose();
//        }
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
        startScreen_Stage.dispose();
        imgTexture.dispose();


    }
}
