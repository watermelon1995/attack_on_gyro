package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.BodyEditorLoader;


/**
 * Created by Michael on 2017/4/16.
 */

public class Gyro extends Actor {
    private String file_name = null;
    public Sprite sprite = null;
    public Body body  = null;
    public Texture texture = null;

    public int hp = 0;
    public final int full_hp = 100;
    public int attrack = 0;
    public boolean isUserControl = false;

    public Gyro(String file_name){
        this.file_name = file_name;

    }

    public void create(float position_x, float position_y, World world){
        //Resize the image to smaller size using pixmap
        Pixmap pixmap =  new Pixmap(Gdx.files.internal(this.file_name));
        Pixmap pixmap_smaller = new Pixmap(100, 100, pixmap.getFormat());
        pixmap_smaller.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, pixmap_smaller.getWidth(), pixmap_smaller.getHeight()
        );
        //Create texture and sprite
        texture = new Texture(pixmap_smaller);
        sprite = new Sprite(texture);
        sprite.setPosition(position_x, position_y);

        //Load specific json file for the physics body
        String fileName_woExtension = this.file_name.replaceFirst("[.][^.]+$", "");
        String fileName_woDirectory = fileName_woExtension.replaceFirst("^[\\w]*\\/", "");
        String debug_fileName = "json/"+fileName_woDirectory+".json";
        BodyEditorLoader bodyEditorLoader = new BodyEditorLoader(Gdx.files.internal(debug_fileName));

        //Create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position_x, position_x);
        bodyDef.linearDamping =2f;
        body = world.createBody(bodyDef);
        body.setAngularVelocity(29f);

        //Create Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        //Load the fixture to the body
        bodyEditorLoader.attachFixture(body, fileName_woDirectory, fixtureDef, pixmap_smaller.getWidth());



        pixmap_smaller.dispose();
        pixmap.dispose();
    }

    public void step(){
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation(body.getAngle()*(float)(180/Math.PI));
    }

    public void move(Vector2 vector2){
        body.setLinearVelocity(vector2);
    }


    public void moveByGravity(){
        float g = 25;
        Vector2 center = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        float distance = body.getPosition().dst(center);
        float force = g / (distance * distance);
        Vector2 direction = center.sub( body.getPosition() );
        body.applyForce( direction.scl(force) , body.getWorldCenter(),true );

    }

    public void setHealthPoint(int hp){
        this.hp = hp;
    }

    public void setUserData(int hp, int attack, boolean isUserControl){
        this.hp = hp;
        this.attrack = attack;
        this.isUserControl = isUserControl;
    }

    public void spinByHP(){
        this.body.setAngularVelocity(Math.min(35f * this.hp / this.full_hp + 10f, 20f ) );
    }

    public void moveBySensor(){
        boolean gyroAvailable = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        if(gyroAvailable){

            float pitch = Gdx.input.getPitch();
            float roll = Gdx.input.getRoll();
            //Convert the raw value to correct direction
            float pitch_correct = pitch * -1;
            float roll_correct = roll ;
            //Make the value larger to make it more sensitive
            pitch_correct *= 12;
            roll_correct *= 12;
            //Only move the gyro when the roll/pitch angle larger than abs(3)
            this.move(new Vector2(pitch_correct, roll_correct));
        }
    }

    public void moveByAI(Vector2 v){
        if (MathUtils.random(1f) > 0.5f) {
            this.move(v);
        }
    }




    public void render(Batch batch){
        sprite.draw(batch);
    }
}
