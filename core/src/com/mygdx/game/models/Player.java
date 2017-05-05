package com.mygdx.game.models;

/**
 * Created by kin on 5/3/17.
 */

public class Player {
    String name;
    boolean isReady;

    public Player(String name, boolean isReady){
        this.name = name;
        this.isReady = isReady;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean equals(Player p){
        if (p == null) return false;
        if (p == this) return true;
        if (p.name.equals(this.name)) return true;
        return false;
    }
}
