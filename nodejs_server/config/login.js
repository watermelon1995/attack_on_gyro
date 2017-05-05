var crypto = require('crypto');
var rand = require('csprng');
var mongoose = require('mongoose');
var gravatar = require('gravatar');
var user = require('./models');

exports.login = function(email,password,callback) {
  user.find({email:email}, function(error, users){
    if (users.length != 0){
      var temp = users[0].salt
      var hash_db = users[0].hashed_password
      var id = users[0].token
      var newpass = temp + password
      var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex")
      var grav_url = gravatar.url(email, {s: '200', r: 'pg', d: '404'})
      if (hash_db == hashed_password){
        callback({'isSuccess':true,'message':"Login Sucess",'token':id});
      }else{
        callback({'isSuccess':false,'message':"Invalid Password", 'token':null});
      }
    }else{
      callback({'isSuccess':false,'message':"User not exist", 'token':null});
    }
  })
}
