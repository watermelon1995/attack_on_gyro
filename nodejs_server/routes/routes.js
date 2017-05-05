
var register = require('../config/register');
var login = require('../config/login');



module.exports = function(app) {
     app.get('/', function(req, res) {

        res.end("Nodejs_server");
    });


    app.post('/login',function(req,res){
        var email = req.body.email;
        var password = req.body.password;
        console.log(email)
        console.log(password);

        login.login(email,password,function (cbresult) {
          if (cbresult.isSuccess){
            res.status(200).json(cbresult);
          }else{
            res.status(400).json(cbresult);
          }

        });
    });


    app.post('/register',function(req,res){
        var email = req.body.email;
        console.log(email);
            var password = req.body.password;
        console.log(password);
        register.register(email,password,function (cbresult) {
            res.status(200).json(cbresult);
          });
    });


};
