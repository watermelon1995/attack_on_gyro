package com.mygdx.game.models;

/**
 * Created by kin on 5/3/17.
 */

public class RegisterResponse {
    public boolean isSuccess;
    public String message;
    public  RegisterResponse(boolean isSuccess, String message){
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
