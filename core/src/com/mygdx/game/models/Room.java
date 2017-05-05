package com.mygdx.game.models;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by kin on 5/3/17.
 */

public class Room {
    public enum STATUS{
        WAITING,
        PLAYING;

        @Override
        public String toString() {
            switch(this) {
                case WAITING: return "WAITING";
                case PLAYING: return "PLAYING";
                default: throw new IllegalArgumentException();
            }
        }
    }

    public static final int maxPlayer = 4;

    public int roomID;

    public String roomName;
    public String roomIdentifer;



    public ArrayList<Player> allplayers;
    public STATUS roomStatus;

    public Room(){
        this.roomID = MathUtils.random(1, 5000);
        this.roomName = "xxxx's Game";
        this.roomStatus = STATUS.WAITING;
        this.allplayers = new ArrayList<Player>();
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomIdentifer() {
        return roomIdentifer;
    }

    public void setRoomIdentifer(String roomIdentifer) {
        this.roomIdentifer = roomIdentifer;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }



    public ArrayList<Player> getAllplayers() {
        return allplayers;
    }

    public void addPlayer(Player p){
        this.allplayers.add(p);
    }

    public STATUS getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        if ("WAITING".equals(roomStatus)){
            this.roomStatus = STATUS.WAITING;
        }else if("PLAYING".equals(roomStatus)){
            this.roomStatus = STATUS.PLAYING;
        }
    }

    public int getPlayerCount(){
        if (this.allplayers == null){
            return 0;
        }
        return this.allplayers.size();

//  if (this.allplayers == null){
//            return 1;
//        }else{
//            return 1 + this.allplayers.length;
//        }
    }
}
