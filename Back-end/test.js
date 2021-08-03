const axios = require('axios')
const qs = require("qs");
const token = 'MzvtjBfo-YgIWbei3GmXJrPXVwxXUL55wVzEfwo9cpgAAAF7A7ViKg'

async function a(){   //access토큰 인증
  const user = await axios({
    method:'GET',
    url:`https://kapi.kakao.com/v2/user/me`, 
    headers:{ 
      Authorization:`Bearer ${token}`
    }
  })
  await console.log(user.data.kakao_account.email)  //카카오 로그인한 사용자의 이메일을 반환
}

async function b(){   //토큰 재발급
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


