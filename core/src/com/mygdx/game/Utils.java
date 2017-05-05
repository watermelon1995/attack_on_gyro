package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mygdx.game.actors.Gyro;
import com.mygdx.game.models.Room;

import java.util.ArrayList;

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

    public static Drawable getBackgroundColor(float r, float g, float b, float a){
        Texture img = new Texture(Gdx.files.internal("img/white_1x1.png"));
        Sprite s = new Sprite(img);
        s.setColor(r,g,b,a);
        SpriteDrawable d = new SpriteDrawable(s);
        return d;
    }

    public static class RoomAdapter extends ArrayListAdapter<Room, VisTable> {

        private final Drawable bg = getBackgroundColor(0,0,0.2f,0);
        private final Drawable selection = getBackgroundColor(126/255f, 229/255f, 229/255f, 0.8f);
        private final BitmapFont font;

        public RoomAdapter(ArrayList<Room> rooms, BitmapFont font){
            super(rooms);
            this.font = font;
            setSelectionMode(SelectionMode.SINGLE);
        }

        @Override
        protected VisTable createView(Room item) {
            if (!VisUI.isLoaded()){
                VisUI.load();
            }
            Label.LabelStyle style = new VisLabel.LabelStyle();
            style.font = this.font;

            VisLabel idLabel = new VisLabel(Integer.toString(item.getRoomID()),style);


            VisLabel roomNameLabel = new VisLabel(item.getRoomName(),style);

            VisLabel statusLabel = new VisLabel(item.getRoomStatus().toString(),style);

            VisLabel playerCountLabel = new VisLabel(Integer.toString(item.getPlayerCount()) + "/" + Room.maxPlayer,style);

            VisTable table = new VisTable();
            table.padLeft(20);
            table.add(idLabel).width(250).center();
            table.add(roomNameLabel).expand().left();
            table.add(statusLabel).expand().right();
            table.add(playerCountLabel).expand().width(100);

            return table;
        }

        @Override
        protected void selectView(VisTable view){
            for (Actor a :view.getChildren()){
                a.setColor(Color.BLACK);
            }
            view.setBackground(selection);
        }

        @Override
        protected void deselectView(VisTable view){
            for (Actor a :view.getChildren()){
                a.setColor(Color.WHITE);
            }
            view.setBackground(bg);
        }
    }
}
