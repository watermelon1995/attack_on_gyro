package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.mygdx.game.AttackOnGyro;
import com.mygdx.game.GifDecoder;
import com.mygdx.game.NetworkUtils;
import com.mygdx.game.Utils;
import com.mygdx.game.models.Player;
import com.mygdx.game.models.Room;
import com.mygdx.game.models.UserModel;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by kin on 5/3/17.
 */

public class GameRoomScreen  implements Screen{

    public enum STATE {
        LOBBY,
        ROOM,
        CHAT,
        LOADING
    }

    final AttackOnGyro game;
    FillViewport viewport;
    Stage gameRoomStage;
    ArrayList<Room> rooms;

    ListView<Room> roomListView;
    String selectedRoomId;

    String myselfID;

    STATE nextState;

    STATE state;

    Room currentRoom;


    public GameRoomScreen(final AttackOnGyro game, String myselfID){
        rooms = new ArrayList<Room>();
        this.myselfID = myselfID;

        this.game = game;
        this.state = STATE.LOBBY;
        this.nextState = STATE.LOBBY;
        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameRoomStage = new Stage(viewport);
        if (!VisUI.isLoaded()) {
            VisUI.load();
        }



        NetworkUtils.setupSocketIO();
        NetworkUtils.clientSocket.on(Socket.EVENT_CONNECT, this.onConnectWSDone);
        NetworkUtils.clientSocket.on("live_room", this.onGetLiveRoomDone);
        NetworkUtils.clientSocket.on("get_room_done", this.onGetCurrentRoomDone);
        NetworkUtils.clientSocket.on("player_joined", this.onOtherJoinRoom);
        NetworkUtils.clientSocket.on("player_left", this.onOtherLeftRoom);
        NetworkUtils.clientSocket.connect();
        System.out.println("Prepare UI");
        prepareUI(state);
        Gdx.input.setInputProcessor(gameRoomStage);


        System.out.println("GameRoomScreen Construct");

    }

    public void prepareUI(STATE s){
        if (s == STATE.LOBBY){
            NetworkUtils.clientSocket.emit("live_room", myselfID);
            generateLobbyUI();
        }else if (s == STATE.ROOM){
            NetworkUtils.clientSocket.emit("get_room", myselfID);
            generateRoomUI();

        }else if (s == STATE.CHAT){

        }else{
            gameRoomStage.clear();
        }
        state = nextState;
    }

    public void displayLoading(float delta){
        elapsed += delta;
        game.batch.draw(game.loadingAnimation.getKeyFrame(elapsed), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }

    public void generateRoomUI(){
        gameRoomStage.clear();
        if (!VisUI.isLoaded()) {
            VisUI.load();
        }
        VisTable uiTable = new VisTable();

        uiTable.pad(50);
        uiTable.setFillParent(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("style/StyleFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size  = 100;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        BitmapFont font = generator.generateFont(parameter);


        VisLabel.LabelStyle labelStyle = new VisLabel.LabelStyle();

        labelStyle.font = font;
        VisLabel headerLabel = new VisLabel(currentRoom.getRoomName(), labelStyle);

        parameter.size  = 30;

        BitmapFont buttonFont = generator.generateFont(parameter);

        Texture notReadyTexture = new Texture(Gdx.files.internal("img/not_ready.png"));
        Sprite notReadySprite = new Sprite(notReadyTexture);

        VisImageTextButton.VisImageTextButtonStyle notReadyStyle = new VisImageTextButton.VisImageTextButtonStyle();
        notReadyStyle.imageUp = new SpriteDrawable(notReadySprite);
        notReadyStyle.imageDown = new SpriteDrawable(notReadySprite);
        notReadyStyle.font = buttonFont;

        Texture readyTexture = new Texture(Gdx.files.internal("img/ready.png"));
        Sprite readySprite = new Sprite(readyTexture);

        VisImageTextButton.VisImageTextButtonStyle readyStyle = new VisImageTextButton.VisImageTextButtonStyle();
        readyStyle.imageUp = new SpriteDrawable(readySprite);
        readyStyle.imageDown = new SpriteDrawable(readySprite);
        readyStyle.font = buttonFont;



        Pixmap p = new Pixmap(300,300, Pixmap.Format.RGB888);
        p.setColor(Color.WHITE);
        p.fillRectangle(0,0,p.getWidth(), p.getHeight());
        p.setColor(Color.BLACK);
        p.fillRectangle(10, 10, p.getWidth()-20, p.getHeight()-20);

        Texture noPlayerTexture = new Texture(p);
        Sprite noPlayerSprite = new Sprite(noPlayerTexture);

        VisImageTextButton.VisImageTextButtonStyle noPlayerStyle = new VisImageTextButton.VisImageTextButtonStyle();
        noPlayerStyle.imageUp = new SpriteDrawable(noPlayerSprite);
        noPlayerStyle.imageDown = new SpriteDrawable(noPlayerSprite);
        noPlayerStyle.font = buttonFont;


        VisImageTextButton[] playerButton = new VisImageTextButton[currentRoom.maxPlayer];


        for (int i = 0; i< currentRoom.maxPlayer;i++){
            if (i < currentRoom.allplayers.size()){
                Player player = currentRoom.getAllplayers().get(i);
                if (player.isReady()){
                    playerButton[i] = new VisImageTextButton(player.getName().substring(0,7), readyStyle);
                    playerButton[i].clearChildren();
                    playerButton[i].add(playerButton[i].getImage()).maxHeight(300).row();
                    playerButton[i].add(playerButton[i].getLabel()).width(400).height(100);
                }else{
                    playerButton[i] = new VisImageTextButton(player.getName().substring(0,7), notReadyStyle);
                    playerButton[i].clearChildren();
                    playerButton[i].add(playerButton[i].getImage()).maxHeight(300).row();
                    playerButton[i].add(playerButton[i].getLabel()).width(400).height(100);
                }
            }else{
                playerButton[i] = new VisImageTextButton("", noPlayerStyle);
                playerButton[i].clearChildren();
                playerButton[i].add(playerButton[i].getImage()).maxHeight(300).row();
                playerButton[i].add(playerButton[i].getLabel()).width(400).height(100);
            }

        }

        uiTable.add(headerLabel).center().colspan(2).expandX().height(210);
        uiTable.row().expandY();
        uiTable.add(playerButton[0]).width(400).expand();
        uiTable.add(playerButton[1]).width(400).expand();
        uiTable.row().expandY();
        uiTable.add(playerButton[2]).width(400).expand();
        uiTable.add(playerButton[3]).width(400).expand();




        gameRoomStage.addActor(uiTable);
    }

    public void generateLobbyUI(){
        if (!VisUI.isLoaded()) {
            VisUI.load();
        }
        gameRoomStage.clear();
        VisTable uiTable = new VisTable();
        uiTable.setDebug(true);

        uiTable.pad(50);
        uiTable.setFillParent(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("style/StyleFont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = Color.valueOf("9f9da3");
        parameter.shadowColor = Color.LIGHT_GRAY;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        BitmapFont headerFont = generator.generateFont(parameter);


        VisLabel.LabelStyle labelStyle = new VisLabel.LabelStyle();

        labelStyle.font = headerFont;

        VisLabel headerLabel = new VisLabel("Game Lobby", labelStyle);
        //128*128

        parameter.size  = 30;
        parameter.color = Color.WHITE;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;

        BitmapFont buttonFont = generator.generateFont(parameter);

        Texture imageTexture = new Texture(Gdx.files.internal("img/create_room.png"));

        VisImageTextButton.VisImageTextButtonStyle buttonStyle = new VisImageTextButton.VisImageTextButtonStyle();
        buttonStyle.imageUp = new SpriteDrawable(new Sprite(imageTexture));
        buttonStyle.imageDown = new SpriteDrawable(new Sprite(imageTexture));
        buttonStyle.font = buttonFont;


        VisImageTextButton createRoomButton = new VisImageTextButton("Create\n\nRoom", buttonStyle);
        createRoomButton.clearChildren();
        createRoomButton.add(createRoomButton.getImage()).maxHeight(100).padTop(-10).row();
        createRoomButton.add(createRoomButton.getLabel()).minWidth(150).padBottom(10);

        VisImageTextButton joinRoomButton = new VisImageTextButton("Join\n\nRoom", buttonStyle);
        joinRoomButton.clearChildren();
        joinRoomButton.add(joinRoomButton.getImage()).maxHeight(100).padTop(-10).row();
        joinRoomButton.add(joinRoomButton.getLabel()).minWidth(150).padBottom(10);

        VisTable buttonTable = new VisTable();
        buttonTable.add(createRoomButton).width(270).height(270);
        buttonTable.row();
        buttonTable.add(joinRoomButton).width(270).height(270);



        Utils.RoomAdapter adapter = new Utils.RoomAdapter(rooms, game.font);
        roomListView = new ListView<Room>(adapter);
        roomListView.getScrollPane().layout();
        roomListView.getScrollPane().setColor(Color.WHITE);
        roomListView.getScrollPane().setWidth(100);

        roomListView.setItemClickListener(new ListView.ItemClickListener<Room>() {
            @Override
            public void clicked(Room item) {
                selectedRoomId = item.getRoomIdentifer();
                System.out.println("Selected Room changed to "+ selectedRoomId);
            }
        });


        uiTable.add(headerLabel).center().colspan(2).expand().height(210);
        uiTable.row();
        uiTable.add(roomListView.getMainTable()).grow().expand();
        uiTable.add(buttonTable);

        createRoomButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                NetworkUtils.clientSocket.emit("create_room" , myselfID);
                nextState = STATE.LOADING;
                System.out.println("Create Room clicked");
            }
        });

        joinRoomButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                NetworkUtils.clientSocket.emit("join_room", selectedRoomId);
                nextState = STATE.LOADING;
                System.out.println("Join Room Clicked");
            }
        });



        gameRoomStage.addActor(uiTable);
        generator.dispose();

    }



    @Override
    public void show() {

    }


    float elapsed;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(state!=nextState){
            prepareUI(nextState);
        }
        gameRoomStage.act(delta);
        game.batch.begin();
        if (state == STATE.LOADING){
            displayLoading(delta);
        }
        gameRoomStage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    Emitter.Listener onGetLiveRoomDone = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("onGetLiveRoomDone");
            JSONObject json = (JSONObject)args[0];
            try{
                JSONArray jsonRooms =  json.getJSONArray("rooms");
                rooms.clear();
                for (int i = 0; i < jsonRooms.length(); ++i) {
                    System.out.println("room data get : "+i);
                    JSONObject jsonRoom = jsonRooms.getJSONObject(i);
                    Room r = new Room();
                    r.setRoomName(jsonRoom.getString("displayName")+"'s Game");
                    r.setRoomStatus(jsonRoom.getString("roomStatus"));
                    JSONArray jsonPlayers = jsonRoom.getJSONArray("players");
                    for (int j = 0 ; j < jsonPlayers.length(); j++){
                        JSONObject jsonPlayer = jsonPlayers.getJSONObject(j);
                        r.addPlayer(new Player(jsonPlayer.getString("name"), jsonPlayer.getBoolean("isReady")));
                    }
                    rooms.add(r);
                }
                roomListView.rebuildView();
            }catch ( Exception e){
                System.out.println("DIU ");
            }
        }
    };

    Emitter.Listener onConnectWSDone = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            NetworkUtils.clientSocket.emit("new_player", myselfID );
        }
    };



    Emitter.Listener onGetCurrentRoomDone = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Json jj = new Json();
            System.out.println("onGetCurrentRoomDone");
            System.out.println(jj.prettyPrint(args));
            JSONObject json = (JSONObject)args[0];
            try{
                Room r = new Room();
                r.setRoomName(json.getString("displayName")+"'s Game");
                r.setRoomStatus(json.getString("roomStatus"));
                JSONArray jsonPlayers = json.getJSONArray("players");
                for (int j = 0 ; j < jsonPlayers.length(); j++){
                    JSONObject jsonPlayer = jsonPlayers.getJSONObject(j);
                    r.addPlayer(new Player(jsonPlayer.getString("name"), jsonPlayer.getBoolean("isReady")));
                }
                currentRoom = r;

                nextState = STATE.ROOM;
            }catch ( Exception e){
                System.out.println("DIU ");
            }

        }
    };

    Emitter.Listener onOtherJoinRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("onOtherJoinRoom");
            JSONObject json = (JSONObject)args[0];
            try{
                Player p = new Player(json.getString("name"), json.getBoolean("isReady"));
                if (currentRoom!= null){
                    currentRoom.allplayers.add(p);
                }
                nextState = STATE.ROOM;
            }catch (Exception e){
                System.out.println("Exception in "+this.toString());
            }
        }
    };

    Emitter.Listener onOtherLeftRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("onOtherLeftRoom");
            JSONObject json = (JSONObject)args[0];
            try{
                Player p = new Player(json.getString("name"), json.getBoolean("isReady"));
                if (currentRoom!= null){
                    currentRoom.allplayers.remove(p);
                }
                nextState = STATE.ROOM;
                generateRoomUI();
            }catch (Exception e){
                System.out.println("Exception in "+this.toString());
            }
        }
    };



    Emitter.Listener onPlayersStatusChanged = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Json j = new Json();
            System.out.println("onPlayersStatusChanged");
            System.out.println(j.prettyPrint(args));
        }
    };
}
