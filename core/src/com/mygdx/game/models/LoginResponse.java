package com.mygdx.game.models;

/**
 * Created by kin on 5/3/17.
 */

public  class LoginResponse {
    public boolean isSuccess;
    public String message;
    public String token;
    public  LoginResponse(boolean isSuccess, String message, String token){
        this.isSuccess = isSuccess;
        this.message = message;
        this.token = token;
    }
}
