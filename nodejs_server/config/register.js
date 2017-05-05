var crypto = require('crypto');
var rand = require('csprng');
var mongoose = require('mongoose');
var user = require('./models');

exports.register = function(email, password, callback){
  var x = email
  //Check email  if is valid
  if(!(x.indexOf("@")==x.length)){
    //Check is password is strong

    //Remove requirement which need symbol in pw
    //&& password.match(/.[!,@,#,$,%,^,&,*,?,_,~]/)
    if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/) && password.length > 4 && password.match(/[0-9]/) ) {
      //Generate a long enough random string
      var temp = rand(160, 36)
      var newpass = temp + password
      var token = crypto.createHash('sha512').update(email +rand).digest("hex")
      var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex")
      

      var new_user = new user({
        token: token,
        email: email,
        hashed_password: hashed_password,
        salt: temp})

      user.find({email: email}, function(error, users){
        var len = users.length;

        if(len == 0){
          new_user.save(function (err) {
            callback({'isSuccess': true, 'message':"Sucessfully Registered"});
          });
        }else{
          callback({'isSuccess': false, 'message':"Email already Registered"});
        }
      })


    }else{
      callback({'isSuccess': false, 'message':"Password weak"});
    }
  }else{
    callback({'isSuccess': false, 'message':"Email Not Valid"});
  }
}
