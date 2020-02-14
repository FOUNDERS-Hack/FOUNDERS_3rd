const express = require('express');
const session = require('express-session');
const path = require('path');
const Web3 = require('web3');
const config = require('./config');
const url = config.getHexlantEndPoint();
const endPoint = url + '/v1/rpc';
const web3 = new Web3(endPoint);
const port = 3000;
const indexRouter = require('./routes/index');
const app = express();

//view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use(session({
    secret: '#1234#',
    resave: false,
    saveUninitialized: false,
    cookie: { secure: false },
  }))

app.use('/', indexRouter); //login = main

app.use(express.static('./css'))

app.listen(port, () => console.log('spermBank web is running'))

module.exports = app;
