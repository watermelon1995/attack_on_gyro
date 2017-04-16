package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Michael on 2017/4/16.
 */

public class Gyro {
    private String file_name = null;
    public Sprite sprite = null;
    public Body body  = null;
    public Texture texture = null;

    public Gyro(String file_name){
        this.file_name = file_name;
    }
    public void create(float position_x, float position_y, World world){
        texture = new Texture(this.file_name);
        sprite = new Sprite(texture);
        sprite.setScale(0.5f, 0.5f);
        sprite.setPosition(position_x, position_y);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position_x, position_x);
        body = world.createBody(bodyDef);
        body.setAngularVelocity(29f);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void step(){
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        sprite.setRotation(body.getAngle()*(float)(180/Math.PI));
    }

    public void move(Vector2 vector2){
        body.setLinearVelocity(vector2);
    }

    public void render(Batch batch){
        sprite.draw(batch);
    }
}
