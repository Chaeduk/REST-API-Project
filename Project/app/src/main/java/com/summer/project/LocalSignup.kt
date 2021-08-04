package com.summer.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_local_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocalSignup : AppCompatActivity() {
    private  lateinit var server: Service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_signup)
        val reg = "^[a-zA-Z0-9]{4,12}\$".toRegex()

        val retrofit = Retrofit.Builder()       //retrofit(Http 연동 - Rest API)
            .baseUrl("http://10.0.2.2:8001/")   //android 로컬 주소(localhost 느낌)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        server = retrofit.create(Service::class.java)


        btn_signup_cancel.setOnClickListener {
//            finish()
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        btn_signup_register.setOnClickListener {
            if(!edt_signup_id.isEnabled){
                if(edt_signup_pwd.text.toString().matches(reg)){
                    if(edt_signup_pwd.text.toString() == edt_signup_pwdok.text.toString()){
                        server.Signup(edt_signup_id.text.toString(), edt_signup_pwd.text.toString()).enqueue(object : Callback<ResponseMSG> {
                            override fun onResponse(
                                call: Call<ResponseMSG>,
                                response: Response<ResponseMSG>
                            ) {
                                if(response.body()?.message.toString() == "good"){
                                   finish()
                                } else{
                                    Toast.makeText(this@LocalSignup, "회원가입이 실패하였습니다. 다시시도해주세요!!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<ResponseMSG>, t: Throwable) {}
                        })
                    }else{
                        Toast.makeText(this,"비밀번호 확인과 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"비밀번호 형식에 맞게 입력해주세요!!", Toast.LENGTH_SHORT).show()
                }

            } else{
                Toast.makeText(this,"아이디 중복을 체크해주세요", Toast.LENGTH_SHORT).show()
            }


        }

        btn_signup_doublecheck.setOnClickListener {
            if(btn_signup_doublecheck.text.toString() == "다시입력"){
                edt_signup_id.isEnabled = true
                btn_signup_doublecheck.text = "중복확인"
                return@setOnClickListener
            }
            if(edt_signup_id.text.toString().matches(reg)){
                server.DoubleCheck(edt_signup_id.text.toString()).enqueue(object : Callback<ResponseMSG> {
                    override fun onResponse(
                        call: Call<ResponseMSG>,
                        response: Response<ResponseMSG>
                    ) {
                        if(response.body()?.message.toString() == "good"){
                            Toast.makeText(this@LocalSignup,"사용가능합니다", Toast.LENGTH_SHORT).show()
                            btn_signup_doublecheck.text = "다시입력"
                            edt_signup_id.isEnabled = false
                        } else{
                            Toast.makeText(this@LocalSignup, "아이디가 중복됩니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResponseMSG>, t: Throwable) {}
                })

            } else{
                Toast.makeText(this, "아이디 형식에 맞게 입력해주세요!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}