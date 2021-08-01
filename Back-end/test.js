const axios = require('axios')

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
a()