const axios = require('axios')
const qs = require("qs");
const token = ''

async function a(){
  const user = await axios({
    method:'GET',
    url:`https://kapi.kakao.com/v2/user/me`, 
    headers:{ 
      Authorization:`Bearer ${token}`
    }
  })
  await console.log(user.data)
}


async function b(){
  const what = await axios({
    method: 'POST',
    url: 'https://kauth.kakao.com/oauth/token',
    data: qs.stringify({
      grant_type: 'refresh_token',
      client_id: '',
      refresh_token: ''
    })  
  })
  await console.log(what)
}

