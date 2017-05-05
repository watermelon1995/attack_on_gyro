package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import com.mygdx.game.AttackOnGyro;
import com.mygdx.game.models.LoginResponse;
import com.mygdx.game.NetworkUtils;
import com.mygdx.game.models.RegisterResponse;

/**
 * Created by kin on 5/2/17.
 */




public class LoginScreen implements Screen {
    final AttackOnGyro game;
    private Stage loginStage;
    private FillViewport viewport;

    AsyncResult<LoginResponse> loginTask;
    boolean isLogining;

    AsyncResult<RegisterResponse> registerTask;
    boolean isRegistering;


    String uname = "qoo238302@gmail.com";
    String upw = "z#6+mydk2016WUTSZKIN";


    public LoginScreen(final AttackOnGyro game){
        this.game = game;
        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        isLogining = false;
        loginStage = new Stage(viewport);
        Gdx.input.setInputProcessor(loginStage);

        if (!VisUI.isLoaded()){
            VisUI.load();
        }
        prepareUI(true);
    }

    public void prepareUI(boolean isLogin){
        loginStage.clear();
        if (isLogin){
            VisTextButton.VisTextButtonStyle textButtonStyle = new VisTextButton.VisTextButtonStyle();
            textButtonStyle.font = game.font;


            VisTextButton loginButton = new VisTextButton("Login", textButtonStyle);
            VisTextButton exitButton = new VisTextButton("Cancel", textButtonStyle);
            VisTextButton regButton = new VisTextButton("Register", textButtonStyle);


            final VisValidatableTextField emailField = new VisValidatableTextField();
            emailField.setText(uname);
            emailField.getStyle().font = game.font;




            final VisValidatableTextField pwField = new VisValidatableTextField();
            pwField.setText(upw);
            pwField.getStyle().font = game.font;
            pwField.setPasswordCharacter('*');
            pwField.setPasswordMode(true);


            Label.LabelStyle labelStyle = new VisLabel.LabelStyle();
            labelStyle.font = game.font;

            VisLabel emailLabel = new VisLabel("Email : ", labelStyle);

            VisLabel pwLabel = new VisLabel("Password : ", labelStyle);


            VisLabel errorText = new VisLabel();
            errorText.setName("errorText");
            errorText.setStyle(labelStyle);
            errorText.setColor(Color.RED);

            VisTable formTable = new VisTable();
            VisTable buttonTable = new VisTable();
            buttonTable.add(regButton).center().expand().fill();
            buttonTable.add(exitButton).center().expand().fill();
            buttonTable.add(loginButton).center().expand().fill();

            formTable.add(emailLabel).pad(20);
            formTable.add(emailField).width(600).height(150).pad(20);
            formTable.row();

            formTable.add(pwLabel).pad(20);
            formTable.add(pwField).width(600).height(150).pad(20);
            formTable.row();

            formTable.add(errorText).pad(20).height(150).colspan(2);
            formTable.row();
            formTable.add(buttonTable).fill().colspan(2).expand().height(150).pad(20);

            SimpleFormValidator validator;
            validator = new SimpleFormValidator(loginButton, errorText, "smooth");
            validator.setSuccessMessage("");
            validator.notEmpty(emailField, "email field cannot be empty");
            validator.notEmpty(pwField, "password field cannot be empty");

            formTable.pad(100);
            formTable.setFillParent(true);
            loginStage.addActor(formTable);

            loginButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    System.out.println(emailField.getText());
                    System.out.println(pwField.getText());
                    isLogining = true;
                    loginTask = game.executor.submit(new AsyncTask<LoginResponse>(){
                        @Override
                        public LoginResponse call() throws Exception {
                            LoginResponse r = NetworkUtils.login(emailField.getText(), pwField.getText());
                            return r;
                        }
                    });
                }
            });


            exitButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    game.setScreen(new StartScreen(game));
                    dispose();
                }
            });

            regButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    prepareUI(false);
                }
            });
        }else{
            VisTextButton.VisTextButtonStyle textButtonStyle = new VisTextButton.VisTextButtonStyle();
            textButtonStyle.font = game.font;


            VisTextButton exitButton = new VisTextButton("Back", textButtonStyle);
            VisTextButton regButton = new VisTextButton("Register", textButtonStyle);


            final VisValidatableTextField emailField = new VisValidatableTextField();
            emailField.getStyle().font = game.font;




            final VisValidatableTextField pwField = new VisValidatableTextField();
            pwField.getStyle().font = game.font;
            pwField.setPasswordCharacter('*');
            pwField.setPasswordMode(true);


            final VisValidatableTextField pwRepeatField = new VisValidatableTextField();
            pwRepeatField.getStyle().font = game.font;
            pwRepeatField.setPasswordCharacter('*');
            pwRepeatField.setPasswordMode(true);

            Label.LabelStyle labelStyle = new VisLabel.LabelStyle();
            labelStyle.font = game.font;

            VisLabel emailLabel = new VisLabel("Email : ", labelStyle);

            VisLabel pwLabel = new VisLabel("Password : ", labelStyle);

            VisLabel pwRepeatLabel = new VisLabel("Re-Type : ", labelStyle);


            final VisLabel errorText = new VisLabel();
            errorText.setName("errorText");
            errorText.setStyle(labelStyle);
            errorText.setColor(Color.RED);

            VisTable formTable = new VisTable();
            VisTable buttonTable = new VisTable();
            buttonTable.add(regButton).center().expand().fill();
            buttonTable.add(exitButton).center().expand().fill();

            formTable.add(emailLabel).pad(20);
            formTable.add(emailField).width(600).height(150).pad(20);
            formTable.row();

            formTable.add(pwLabel).pad(20);
            formTable.add(pwField).width(600).height(150).pad(20);
            formTable.row();

            formTable.add(pwRepeatLabel).pad(20);
            formTable.add(pwRepeatField).width(600).height(150).pad(20);
            formTable.row();

            formTable.add(errorText).pad(20).height(150).colspan(2);
            formTable.row();
            formTable.add(buttonTable).fill().colspan(2).expand().height(150).pad(20);

            SimpleFormValidator validator;
            validator = new SimpleFormValidator(regButton, errorText, "smooth");
            validator.setSuccessMessage("");
            validator.notEmpty(emailField, "email field cannot be empty");
            validator.notEmpty(pwField, "password field cannot be empty");
            validator.notEmpty(pwRepeatField, "re-type password field cannot be empty");


            formTable.pad(100);
            formTable.setFillParent(true);
            loginStage.addActor(formTable);


            exitButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    prepareUI(true);
                }
            });

            regButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    System.out.println(pwField.getText());
                    System.out.println(pwRepeatField.getText());
                    if (pwField.getText().equals(pwRepeatField.getText()) && pwField.getText().length()!=0){

                        isRegistering = true;
                        registerTask = game.executor.submit(new AsyncTask<RegisterResponse>(){
                            @Override
                            public RegisterResponse call() throws Exception {
                                RegisterResponse r = NetworkUtils.register(emailField.getText(), pwField.getText());
                                return r;
                            }
                        });
                    }else{
                        errorText.setText("Password not match");
                        errorText.setColor(Color.RED);
                    }
                }
            });
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (isLogining){
            if (loginTask.isDone()){
                if (loginTask.get().isSuccess){
                    game.setScreen(new GameRoomScreen(game, loginTask.get().token));
                    dispose();
                }else{
                    VisLabel errorText = loginStage.getRoot().findActor("errorText");
                    errorText.setText(loginTask.get().message);
                    errorText.setColor(Color.RED);
                }
                isLogining = false;
            }
        }
        if (isRegistering){
            if (registerTask.isDone()){
                if (registerTask.get().isSuccess){
                    prepareUI(true);
                    VisLabel errorText = loginStage.getRoot().findActor("errorText");
                    errorText.setText(registerTask.get().message);
                    errorText.setColor(Color.GREEN);
                }else{
                    VisLabel errorText = loginStage.getRoot().findActor("errorText");
                    errorText.setText(registerTask.get().message);
                    errorText.setColor(Color.RED);
                }

                isRegistering = false;
            }
        }


        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        loginStage.act(delta);
        game.batch.begin();
        loginStage.draw();
        game.batch.end();

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
        loginStage.dispose();
        VisUI.dispose();
    }
}
