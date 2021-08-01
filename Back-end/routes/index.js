const express = require('express')
const axios = require('axios')

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
    url:`https://kapi.kakao.com/v2/user/me`, 
    headers:{ 
      Authorization:`Bearer ${token}`
    }
  })
  const email = user.data.kakao_account.email
  return email
}

router.post('/google', async (req, res) => {  //POST /google
  const user = await verify(req.body.token)   //토큰을 이용해 구글 이메일 주소를 받아옴
  res.send({"user": user})
})

router.post('/kakao', async (req, res) => {   //POST /kakao
   const access_token = req.body.access_token //토큰을 이용해 카카오 이메일 주소를 받아옴(애플리케이션 설정에서 권한 필요)
   const user = await kakaoAuth(access_token)
   res.send({"user": user}) 
})

//추후에 구현해야할 것 : access 토큰이 만료된다면 refresh 토큰을 이용하여 update 해주는 알고리즘 구현

module.exports = router