/**
 * Module dependencies.
 */
var express  = require('express');
var connect = require('connect-logger');
var app      = express();
var port     = process.env.PORT || 8080;
var bodyParser = require('body-parser');
var server = require('http').Server(app);
var io = require('socket.io')(server);

console.log(connect)
// Configuration
app.use(express.static(__dirname + '/public'));
app.use(connect({}));
app.use(bodyParser.json());
// app.use(connect.json());
// app.use(connect.urlencoded());

// Routes

require('./routes/routes.js')(app);

var playerList = [];
var roomList = [];

function Player(_name, _socket){
  this.name = _name;
  this.socket = _socket;
  this.room = null;
  this.isReady = false;
}

//
function Room(_name, _maxPlayer, _roomId){
  this.name = _roomId;
  this.displayName = _name;
  this.maxPlayer = _maxPlayer;
  this.playerCount = 0;
  this.players = [];
  this.roomStatus = 'WAITING';
}


server.listen(port);


var server_socket = io.of('/game');
//When the player connect to the game api
server_socket.on('connection', function(player_socket){
  var this_player = null;
  player_socket.on("new_player", function(data){
    console.log("on new_player")
    var new_player = new Player(data, player_socket);
    this_player = new_player;
    playerList.push(new_player);
  });

  player_socket.on('create_room', function(playerID){
    console.log("on create_room")
    var new_room = new Room(playerID.substring(0, 7), 4, player_socket.id);
    new_room.players.push(this_player);
    new_room.playerCount += 1;
    roomList.push(new_room);

    this_player.room = new_room.name;

    var sendableRoomList = [];
    for (var i = 0 ; i < roomList.length; i++ ){
      var playersInRoom = [];
      for (var j = 0 ; j < roomList[i].players.length; j++){
        var item = {
          name: roomList[i].players[j].name,
          isReady: roomList[i].players[j].isReady
        };
        playersInRoom.push(item);
      }
      sendableRoomList.push({
        displayName: roomList[i].displayName,
        playerCount: roomList[i].playerCount,
        players: playersInRoom,
        roomStatus: roomList[i].roomStatus
      });
    }
    var sendableRoom = {};
    sendableRoom['displayName'] = new_room.displayName;
    sendableRoom['roomStatus'] = new_room.roomStatus;
    sendableRoom['players'] = [];
    for (var i = 0; i < new_room.players.length ;i++){
      sendableRoom['players'].push({
        name: new_room.players[i].name,
        isReady: new_room.players[i].isReady
      });
    }
    player_socket.emit("get_room_done", sendableRoom)

    player_socket.broadcast.emit('live_room', {rooms: sendableRoomList});
    console.log("broadcast roomList");

  });
  // console.log(player_socket);
  player_socket.on('join_room', function(roomID){
    console.log("on join_room")
    //Check room is full or not
    var room = roomList.find(roomID);
    if (room != null){
      player_socket.join(roomID);
      room.players.push(this_player);
      room.playerCount += 1;
      this_player.room = room.name;

      var sendableRoom = {};
      sendableRoom['displayName'] = room.displayName;
      sendableRoom['roomStatus'] = room.roomStatus;
      sendableRoom['players'] = [];
      for (var i = 0; i < room.players.length ;i++){
        sendableRoom['players'].push({
          name: room.players[i].name,
          isReady: room.players[i].isReady
        });
      }
      player_socket.broadcast.to(roomID).emit("get_room_done", sendableRoom);
    }
  });


  player_socket.on('leave_room', function(roomID){
    console.log("on leave_room")
    var room = roomList.find(roomID);
    if (room != null){
      room.players.remove(this_player);
      room.playerCount -= 1;
      if (room.playerCount <= 0){
        roomList.remove(room);
        //Send room List info
        var sendableRoomList = [];
        for (var i = 0 ; i < roomList.length; i++ ){
          var playersInRoom = [];
          for (var j = 0 ; j < roomList[i].players.length; j++){
            var item = {
              name: roomList[i].players[j].name,
              isReady: roomList[i].players[j].isReady
            };
            playersInRoom.push(item);
          }
          sendableRoomList.push({
            displayName: roomList[i].displayName,
            playerCount: roomList[i].playerCount,
            players: playersInRoom,
            roomStatus: roomList[i].roomStatus
          });
        }
        player_socket.broadcast.emit("live_room", sendableRoomList);
        console.log("room deleted");

      }else{
        //Send room info
        var sendableRoom = {};
        sendableRoom['displayName'] = room.displayName;
        sendableRoom['roomStatus'] = room.roomStatus;
        sendableRoom['players'] = [];
        for (var i = 0; i < room.players.length ;i++){
          sendableRoom['players'].push({
            name: room.players[i].name,
            isReady: room.players[i].isReady
          });
        }
        player_socket.broadcast.to(roomID).emit("player_room_left", sendableRoom);
      }
      this_player.room = null;
      this_player.isReady = false;
      player_socket.leave(roomID);

    }
  });

  player_socket.on('get_room', function(playerID){
    console.log("on get_room")
    var room = roomList.find(this_player.room)

    var sendableRoom = {};
    sendableRoom['displayName'] = room.displayName;
    sendableRoom['roomStatus'] = room.roomStatus;
    sendableRoom['players'] = [];
    for (var i = 0; i < room.players.length ;i++){
      sendableRoom['players'].push({
        name: room.players[i].name,
        isReady: room.players[i].isReady
      });
    }
    player_socket.emit("get_room_done", sendableRoom)
  });

  player_socket.on('player_ready', function(playerID){
    var player = playerList.find(playerID);
    player.isReady = !player.isReady;

    var sendablePlayer = {};
    sendablePlayer['name'] = player.name;
    sendablePlayer['isReady'] = player.isReady;

    player_socket.broadcast.to(player.room).emit("player_is_reaady", sendablePlayer);
  });

  player_socket.on('live_room', function(data){
    console.log("on live_room")
    var sendableRoomList = [];
    for (var i = 0 ; i < roomList.length; i++ ){
      var playersInRoom = [];
      for (var j = 0 ; j < roomList[i].players.length; j++){
        var item = {
          name: roomList[i].players[j].name,
          isReady: roomList[i].players[j].isReady
        };
        playersInRoom.push(item);
      }
      sendableRoomList.push({
        displayName: roomList[i].displayName,
        playerCount: roomList[i].playerCount,
        players: playersInRoom,
        roomStatus: roomList[i].roomStatus
      });
    }
    player_socket.emit('live_room', {rooms: sendableRoomList});
  });

  player_socket.on('live_player', function(){
    console.log("on live_player")
    player_socket.emit('live_player', {'player' : playerList});
  });

  player_socket.on('disconnect', function(){
    console.log("on disconnect");
    if(this_player!=null){

      console.log("Player : "+this_player.name + " disconnected");
      if (this_player.room!=null){
        var room = roomList.find(this_player.room);
        roomList.remove(room);

        console.log("Room created by disconnected user is also removed");

      }

      var sendableRoomList = [];
      for (var i = 0 ; i < roomList.length; i++ ){
        var playersInRoom = [];
        for (var j = 0 ; j < roomList[i].players.length; j++){
          var item = {
            name: roomList[i].players[j].name,
            isReady: roomList[i].players[j].isReady
          };
          playersInRoom.push(item);
        }
        sendableRoomList.push({
          displayName: roomList[i].displayName,
          playerCount: roomList[i].playerCount,
          players: playersInRoom,
          roomStatus: roomList[i].roomStatus
        });
      }
      player_socket.broadcast.emit('live_room', {rooms: sendableRoomList});

    }
    playerList.remove(this_player);

  });
});



// Add find by name function for arrays (to find player or room)
Array.prototype.find = function(name) {
	for (var i = 0; i < this.length; i++) {
		if (name == this[i].name) { return this[i]; }
	}
  return null;
};

Array.prototype.remove = function(target){
  if (target!=null){
    var targetIndex = this.indexOf(target);
    if (targetIndex > -1){
      this.splice(targetIndex, 1);
    }
  }
}








console.log('The App runs on port ' + port);
setInterval(function() {
  console.log("playerList: ");
  console.log(playerList);
  console.log("roomList: ");
  console.log(roomList);
}, 10000, "DIU");
