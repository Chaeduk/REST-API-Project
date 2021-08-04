const express = require('express')
var User = require('../schemas/user')
const axios = require('axios')
const bcrypt = require('bcrypt')

const router = express.Router()

const {OAuth2Client} = require('google-auth-library')
const client = new OAuth2Client(process.env.server_client_id)
async function verify(k){
  const ticket = await client.verifyIdToken({
    idToken: k,
    audience: process.env.server_client_id
  })
  const payload = ticket.getPayload()
  const userid = payload['email']
  return userid
}

async function kakaoAuth(token){   
  const user = await axios({
    method:'GET',
    url:'https://kapi.kakao.com/v2/user/me', 
    headers:{ 
      Authorization:`Bearer ${token}`
    }
  })
  const email = user.data.kakao_account.email
  return email
}

router.post('/google', async (req, res) => {  //POST /google
  const user = await verify(req.body.token)   //토큰을 이용해 구글 이메일 주소를 받아옴(refresh토큰을 찾지못하여서 내가 다시 토큰 발행)
  const member = await User.find({account: user, login_path: 'google'})
  if(Object.keys(member).length == 0 ){
    const new_member = new User({
      account: user,
      login_path: 'google'
    })
    await new_member.save()
    res.send({"token": new_member._id, "message": 'register'})
  } else if(member.area == null || member.nickname == null || member.profile_URL == null){
    res.send({"token": member[0]._id, "message": 'register'})
  } else{
    res.send({"token": member[0]._id, "message": 'complete register'})
  }
})

router.post('/kakao', async (req, res) => {   //POST /kakao
   const access_token = req.body.access_token //토큰을 이용해 카카오 이메일 주소를 받아옴(애플리케이션 설정에서 권한 필요)
   const user = await kakaoAuth(access_token)
   const member = await User.find({account: user, login_path: 'kakao'})
   if(Object.keys(member).length == 0){
     const new_member = new User({
       account: user,
       login_path: 'kakao'
     })
     await new_member.save()
     res.send({"token": new_member._id, "message": 'register'})
   } else if(member.area == null || member.nickname == null || member.profile_URL == null){
    res.send({"token": member[0]._id, "message": 'register'})
  } else{
    res.send({"token": member[0]._id, "message": 'complete register'})
  }
})

router.post('/local', async(req, res)=>{    //POST /local
  const member = await User.findOne({account: req.body.id, login_path: 'local' })    //암호화 해서 찾기
  if(member == null){
    res.send({"token": "","message": "no"})
  } else{
      const result = await bcrypt.compare(req.body.pwd,member.password)
    if(!result) {
      res.send({"token": "","message": "no"})
    } else if(member.area == null || member.nickname == null || member.profile_URL == null){
      res.send({"token": member._id, "message": 'register'})
    } else{
      res.send({"token": member._id, "message": 'complete register'})
    }
  }
  
})

router.post('/signup', async(req, res)=>{   //POST /signup
  const hash = await bcrypt.hash(req.body.pwd, 12)
  const new_member = new User({
    account: req.body.id,
    password: hash,    //암호화 하기
    login_path: 'local'
  })
  await new_member.save()
  res.send({"message":"good"})
})

router.get('/doublecheck', async(req, res)=>{   //GET /doublecheck
  const member = await User.find({account: req.query.id, login_path: 'local'})
  if(Object.keys(member).length == 0){
    res.send({"message": "good"})
  } else{
    res.send({"message": "duplicated"})
  }
})



//추후에 구현해야할 것 : access 토큰이 만료된다면 refresh 토큰을 이용하여 update 해주는 알고리즘 구현

module.exports = router


