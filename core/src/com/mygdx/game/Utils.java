package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.actors.Gyro;

/**
 * Created by kin on 4/16/17.
 */

public class Utils {
    public  static  float RAD_TO_DEG(float radian){
        return (float) (radian * 180f / Math.PI);
    }
    public static boolean IS_OUT_SCREEN(Gyro actor){
        if (actor.body.getPosition().x < 0 - actor.sprite.getWidth()|| actor.body.getPosition().x > Gdx.graphics.getWidth()){
            return true;
        }else if (actor.body.getPosition().y < 0 - actor.sprite.getHeight() || actor.body.getPosition().y > Gdx.graphics.getHeight()){
            return  true;
        }else{
            return false;
        }
    }
}
