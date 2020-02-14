const express = require('express');
const router = express.Router();
const config = require('../config');

// move to main page
router.get('/', async function(req, res, next) {
    res.render('index.ejs');
    console.log('Index Page open')
})

// move to history page
router.get('/history', function(req, res) {
    res.render('history.ejs');
    console.log('History Page open')
})

// 
module.exports = router;