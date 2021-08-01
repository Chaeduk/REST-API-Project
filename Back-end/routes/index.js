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

async function kakaoAuth(token){   //access토큰 인증
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

router.post('/google', async (req, res) => {
  const user = await verify(req.body.token)
  res.send({"user": user})
})

router.post('/kakao', async (req, res) => {
   const access_token = req.body.access_token
   const user = await kakaoAuth(access_token)
   res.send({"user": user}) 
})

module.exports = router