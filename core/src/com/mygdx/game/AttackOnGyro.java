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
	Sprite sprite;
	Texture img;
    World world;
	BitmapFont bitmapFont;
    Body body;


	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);
        sprite.setScale(0.5f, 0.5f);

        //Set position to the middle of the screen
        sprite.setPosition(Gdx.graphics.getWidth()/2 - sprite.getWidth()/2,
                            Gdx.graphics.getHeight()/2 );
        //World is the heart of the physics
        world = new World(new Vector2(0f,0f),true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;


        bodyDef.position.set(sprite.getX(), sprite.getY());
        //Apply the body defination to our body
        body = world.createBody(bodyDef);
        body.setAngularVelocity(25f);

        //Define the shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        //Create a fixture defination to applt to our body
        FixtureDef fixtureDef = new FixtureDef();
        //Can define other physics properites
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);




        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(3.0f, 3.0f);
        //Shape can dispose
        shape.dispose();
	}

	public  static  float convertRadToDeg(float radian){
        return (float) (radian * 180f / Math.PI);
    }

	@Override
	public void render () {

        world.step(Gdx.graphics.getDeltaTime(),6,2);
        //Update sprite position
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation(body.getAngle()*(float) (180/Math.PI));

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
        sprite.draw(batch);
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
            pitch_correct *= 9;
            roll_correct *= 9;
            bitmapFont.draw(batch, "Pitch: " + pitchText, 400, 500);
            bitmapFont.draw(batch, "Roll: "+rollText, 700, 500);

            body.setLinearVelocity(new Vector2(pitch_correct, roll_correct));
        }



		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
        world.dispose();
	}
}
