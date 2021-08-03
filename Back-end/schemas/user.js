const mongoose = require('mongoose');

const { Schema } = mongoose
const userSchema = new Schema({
  account: {
    type: String, //로컬로 로그인 한 경우 아이디이고, 외부 로그인을 이용한 경우 이메일이 된다.
    required: true,
  },
  login_path: {   //로컬, 구글, 카카오가 있을 수 있다.
    type: String,
    required: true,
  },
  password: {
    type: String, //로컬로그인에만 있다
  },
  area: {
    type: String, //회원이 거주하는 지역
  },
  nickname: {
    type: String, //애플리케이션에서 회원이 쓰는 이름
    unique: true,
  },
  profile_URL: {
    type: String,
  },
})

module.exports = mongoose.model('User', userSchema)