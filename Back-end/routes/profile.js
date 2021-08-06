const express = require('express')
var User = require('../schemas/user')

const router = express.Router()

router.get('/doublecheck', async (req, res) =>{
  const member = await User.find({nickname: req.query.id})
  if(Object.keys(member).length == 0){
    res.send({"message": "good"})
  } else{
    res.send({"message": "duplicated"})
  }
})

router.post('/register', async (req, res)=>{
  await User.updateOne({_id : req.body.id}, {profile_URL:req.body.url, nickname: req.body.nickname, area: req.body.area})
  res.send({"token": req.body.id, "message": 'register'})
})

module.exports = router