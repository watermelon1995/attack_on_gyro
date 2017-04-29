package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.AttackOnGyro;
import com.mygdx.game.Contrains;
import com.mygdx.game.Utils;

/**
 * Created by kin on 4/16/17.
 */

public class GameStage extends Stage {
    final AttackOnGyro game;
    private World world;

    private final float TIME_STEP = 1/300f;
    private float accumulator = 0f;
    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;
    private Sound sound;
    public boolean freeze = false;
    public int freeze_counter = 0;

    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private com.mygdx.game.actors.Gyro playerGyro;

    private com.mygdx.game.actors.Gyro enemyGyro;

    public GameStage (final  AttackOnGyro game){
        this.game = game;
        sound = Gdx.audio.newSound(Gdx.files.internal("sound/sword2.m4a"));
        //Crete world which is a physics container
        world = new World(Contrains.WORLD_GRAVITY,true);
        //Create actor player
        playerGyro = new com.mygdx.game.actors.Gyro("img/top2.png");
        playerGyro.create(200, 200, world);
        playerGyro.setUserData(100, 10, true);
        this.addActor(playerGyro);

        //Create actor enemy
        enemyGyro = new com.mygdx.game.actors.Gyro("img/top3.gif");
        enemyGyro.create(600,600, world);
        enemyGyro.setHealthPoint(100);
        enemyGyro.setUserData(100, 10, false);
        this.addActor(enemyGyro);

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        renderer = new Box2DDebugRenderer();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0f);
        camera.update();

        //Overwrite contactListener to play sound and deduct hp
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getBody() == playerGyro.body && contact.getFixtureB().getBody() == enemyGyro.body){
                    playerGyro.body.applyLinearImpulse(playerGyro.body.getPosition().add(enemyGyro.body.getPosition()).scl(50f), playerGyro.body.getWorldCenter(), true);
                    enemyGyro.body.applyLinearImpulse(enemyGyro.body.getPosition().add(playerGyro.body.getPosition()).scl(50f), enemyGyro.body.getWorldCenter(), true);
                    sound.play(0.5f);
                    freeze = true;

                }
            }

            @Override
            public void endContact(Contact contact) {
                enemyGyro.setHealthPoint(enemyGyro.hp - MathUtils.random(1, playerGyro.attrack));
                playerGyro.setHealthPoint(playerGyro.hp - MathUtils.random(1, enemyGyro.attrack));
                playerGyro.spinByHP();
                enemyGyro.spinByHP();
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }


    @Override
    public void act(float delta){
        super.act(delta);
        accumulator += delta;
        while (accumulator >= delta){
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
        if (!freeze){
            
            playerGyro.moveBySensor();

            enemyGyro.moveByAI(playerGyro.body.getPosition().sub(enemyGyro.body.getPosition()).scl(0.5f));


        }else{
            freeze_counter++;
            if (freeze_counter > 60){
                freeze = false;
                freeze_counter = 0;
            }
        }
        enemyGyro.step();
        playerGyro.step();





        game.batch.begin();
        game.font.draw(game.batch, "Your HP: " +  Integer.toString(playerGyro.hp), Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight()/2 -100);
        game.font.draw(game.batch, "Enemy HP: " +  Integer.toString(enemyGyro.hp), Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight()/2 -200);
        game.batch.end();

        if (Utils.IS_OUT_SCREEN(enemyGyro) || enemyGyro.hp <0 ){
            game.setScreen(new com.mygdx.game.screens.GameOverScreen(game, true));
        }else if (Utils.IS_OUT_SCREEN(playerGyro) || playerGyro.hp <0){
            game.setScreen(new com.mygdx.game.screens.GameOverScreen(game, false));
        }else{

        }

    }

    @Override
    public void draw(){
        super.draw();

        this.getBatch().begin();
        playerGyro.render(this.getBatch());
        enemyGyro.render(this.getBatch());
        this.getBatch().end();
        renderer.render(world, camera.combined);
    }
}
