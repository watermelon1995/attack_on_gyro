package com.mygdx.game;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;


import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;


import com.mygdx.game.models.LoginResponse;
import com.mygdx.game.models.RegisterResponse;
import com.mygdx.game.models.UserModel;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by kin on 5/2/17.
 */



public class NetworkUtils {

    public static Socket clientSocket;

    public final static String server_address = "http://192.168.0.100:8080";
    public final static String loginAPI = server_address+"/login";
    public final static String registerAPI = server_address+"/register";
    public final static String socketAPI = server_address+"/game";

    public static LoginResponse login(String email, String password) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        LoginResponse loginResponse = null;
        try{
            //Prepare JsonObject
            UserModel user = new UserModel(email, password);
            Json userJson = new Json();

            userJson.setOutputType(JsonWriter.OutputType.json);

            if (userJson!=null){
                URL loginURL = new URL(loginAPI);
                connection =  (HttpURLConnection)loginURL.openConnection();
                connection.setReadTimeout(2000);
                connection.setConnectTimeout(2000);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(userJson.toJson(user));
                outputStream.flush();
                outputStream.close();
                //Get response
                int statusCode = connection.getResponseCode();
                if (statusCode == 200){
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    JsonReader jr = new JsonReader();
                    JsonValue response = jr.parse(inputStream);


                    System.out.println("------------------------------");
                    System.out.println(response.getString("message"));
                    System.out.println(response.getString("token"));
                    System.out.println("------------------------------");
                    inputStream.close();
                    loginResponse = new com.mygdx.game.models.LoginResponse(true,response.getString("message"), response.getString("token") );
                    System.out.println("login success");


                }else{
                    System.out.println("failed to login");
                    loginResponse = new com.mygdx.game.models.LoginResponse(false, "invalid password", null);
                }



            }

        }catch (Exception e){
            loginResponse = new com.mygdx.game.models.LoginResponse(false, "exception in client", null);
        }finally {
            if (connection!= null){
                connection.disconnect();
            }

        }
        return loginResponse ;
    }

    public static RegisterResponse register(String email, String password) {
        HttpURLConnection connection = null;
        InputStream inputStream;
        RegisterResponse registerResponse = null;
        try{
            //Prepare JsonObject
            com.mygdx.game.models.UserModel user = new com.mygdx.game.models.UserModel(email, password);
            Json userJson = new Json();

            userJson.setOutputType(JsonWriter.OutputType.json);

            if (userJson!=null){
                URL registerURL = new URL(registerAPI);
                connection =  (HttpURLConnection)registerURL.openConnection();
                connection.setReadTimeout(2000);
                connection.setConnectTimeout(2000);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(userJson.toJson(user));
                outputStream.flush();
                outputStream.close();

                System.out.println("he");
                //Get response
                int statusCode = connection.getResponseCode();
                if (statusCode == 200 ){
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    JsonReader jr = new JsonReader();
                    JsonValue response = jr.parse(inputStream);


                    System.out.println("------------------------------");
                    System.out.println(response.getString("message"));
                    System.out.println("------------------------------");
                    inputStream.close();
                    registerResponse = new RegisterResponse(response.getBoolean("isSuccess"),response.getString("message"));



                }else {
                    System.out.println("failed to register");
                    registerResponse = new RegisterResponse(false,"Server Error");
                }



            }

        }catch (Exception e){
            System.out.println(e.getStackTrace());
            registerResponse = new RegisterResponse(false,"Client Exception");
        }finally {
            if (connection!= null){
                connection.disconnect();
            }

        }
        return registerResponse ;
    }

    public static void setupSocketIO(){
        try{
            clientSocket = IO.socket(socketAPI);
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }


}
