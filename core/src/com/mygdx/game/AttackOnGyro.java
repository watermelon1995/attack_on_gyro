package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class AttackOnGyro extends ApplicationAdapter {
	SpriteBatch batch;
	World world;
	BitmapFont bitmapFont;

    Gyro gyro;
    Gyro gyro2;

	@Override
	public void create () {
		batch = new SpriteBatch();
        //World is the heart of the physics
        world = new World(new Vector2(0f,0f),true);


        gyro = new Gyro("top2.jpg");
        gyro.create(100, 100, world);

        gyro2 = new Gyro("top2.jpg");
        gyro2.create(Gdx.graphics.getWidth()/2 ,
                Gdx.graphics.getHeight()/2, world);

        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(3.0f, 3.0f);
	}

	public  static  float convertRadToDeg(float radian){
        return (float) (radian * 180f / Math.PI);
    }

	@Override
	public void render () {

        world.step(Gdx.graphics.getDeltaTime(),6,2);
        //Update sprite position
        gyro.step();
        gyro2.step();


		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

        gyro.render(batch);
        gyro2.render(batch);
//        batch.draw(sprite, sprite.getX(), sprite.getY());

        boolean gyroAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        if(gyroAvailable){

            String pitchText = String.format("%.2f", Gdx.input.getPitch());
            String rollText = String.format("%.2f", Gdx.input.getRoll());
            float pitch = Gdx.input.getPitch();
            float roll = Gdx.input.getRoll();
            //Convert the raw value to correct direction
            float pitch_correct = pitch * -1;
            float roll_correct = roll ;
            //Make the value larger to make it more sensitive
            pitch_correct *= 15;
            roll_correct *= 15;
            bitmapFont.draw(batch, "Pitch: " + pitchText, 400, 500);
            bitmapFont.draw(batch, "Roll: "+rollText, 700, 500);

            gyro2.move(new Vector2(pitch_correct, roll_correct));
        }



		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        world.dispose();
	}
}
