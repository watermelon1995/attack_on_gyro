package com.mygdx.game.models;



/**
 * Created by kin on 5/2/17.
 */

public class UserModel {
    private String email;
    private String password;

    public UserModel(String email, String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }


}
