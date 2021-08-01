const express = require('express')

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

router.post('/', async (req, res) => {
  const user = await verify(req.body.name)
  res.send({"user": user})
})



module.exports = router