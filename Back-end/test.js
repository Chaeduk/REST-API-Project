const {OAuth2Client} = require('google-auth-library')
const client = new OAuth2Client("")
async function verify(){
  const ticket = await client.verifyIdToken({
    idToken: "",
    audience: ""
  })
  const payload = ticket.getPayload()
  const userid = payload['email']
  console.log(userid)
}
verify()