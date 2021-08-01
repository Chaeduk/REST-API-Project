package com.summer.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.gson.JsonElement
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123
    private lateinit var idToken : String
    private  lateinit var server: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_login.setOnClickListener {
            signIn()
        }

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                //Login Fail
            }
            else if (token != null) {
                //Login Success
                Log.i("success",token.accessToken)
                UserApiClient.instance.me{ user, error ->
                    Log.i("hi", user.toString())

                }
            }
        }


        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        kakao_login.setOnClickListener {
            LoginClient.instance.run{
                if(isKakaoTalkLoginAvailable(this@MainActivity)){
                    loginWithKakaoTalk(this@MainActivity, callback =callback)
                }else{
                    loginWithKakaoAccount(this@MainActivity, callback =callback)
                }
            }
        }


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        server = retrofit.create(Service::class.java)
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null && account.id != null) {

        }

    }

    private fun signIn():Unit{
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                idToken = account.idToken
                server.getRequest(idToken).enqueue(object : Callback<JsonElement>{
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        Log.i("info",response.body().toString())
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    }

                })
            }catch (e: ApiException){

            }
        }
    }

}