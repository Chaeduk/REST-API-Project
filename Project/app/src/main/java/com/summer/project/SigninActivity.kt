package com.summer.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SigninActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123
    private lateinit var idToken : String
    private  lateinit var server: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_signin_google.setOnClickListener {       //구글 로그인 버튼 click
            signIn()
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                //Login Fail
            }
            else if (token != null) {
                //Login Success
                Log.i("success",token.toString())
                server.KaKaoAuth(token.accessToken, token.refreshToken).enqueue(object : Callback<ResponseDTO>{  //카카오 로그인으로 얻은 토큰들을 서버로 전달
                    override fun onResponse(
                        call: Call<ResponseDTO>,
                        response: Response<ResponseDTO>
                    ) {
                        Log.i("info", response.body()?.message.toString())        //서버의 응답
                    }

                    override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                    }

                })
            }
        }

//        UserApiClient.instance.me{ user, error ->     카카오톡에서 로그인된 유저정보를 가져올 수 있는 코드
//            Log.i("hi", user.toString())
//
//        }


        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        btn_signin_kakao.setOnClickListener {        //카카오 로그인 버튼 click
            LoginClient.instance.run{
                if(isKakaoTalkLoginAvailable(this@SigninActivity)){   //카카오톡 앱이 있는지 여부
                    loginWithKakaoTalk(this@SigninActivity, callback =callback)   //카카오톡이 설치되어 있으면 앱에서 로그인 처리
                }else{
                    loginWithKakaoAccount(this@SigninActivity, callback =callback)    //카카오톡이 설치되어 있지않으면 웹으로 처리
                }
            }
        }


        val retrofit = Retrofit.Builder()       //retrofit(Http 연동 - Rest API)
            .baseUrl("http://10.0.2.2:8001/")   //android 로컬 주소(localhost 느낌)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        server = retrofit.create(Service::class.java)


        btn_signin_register.setOnClickListener {
            val intent = Intent(this, LocalSignup::class.java)
            startActivity(intent)
        }


    }

//    override fun onStart() {
//        super.onStart()
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if (account != null && account.id != null) {
//
//        }
//
//    }

    private fun signIn():Unit{      //구글 로그인 처리 함수
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   //구글 로그인 후
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                idToken = account.idToken       //토큰 아이디를 받아옴(토큰 만료여부 확인하기)
                server.GoogleAuth(idToken).enqueue(object : Callback<ResponseDTO>{  //구글 로그인으로 얻은 토큰을 서버로 전달
                    override fun onResponse(
                        call: Call<ResponseDTO>,
                        response: Response<ResponseDTO>
                    ) {
                        Log.i("info",response.body()?.message.toString())        //서버의 응답
                    }

                    override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {

                    }

                })
            }catch (e: ApiException){

            }
        }
    }

}