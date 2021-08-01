package com.summer.project

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123
    private lateinit var idToken : String
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
//                networking()
            }catch (e: ApiException){

            }
        }
    }

//    fun networking():Unit{
//        thread(start=true){
//            try{
//                val url = URL("http://localhost:8001/")
//
//                val urlConnection = url.openConnection() as HttpURLConnection
//                urlConnection.requestMethod = "GET"
//
//                if(urlConnection.responseCode == HttpURLConnection.HTTP_OK){
//                    val streamReader = InputStreamReader(urlConnection.inputStream)
//                    val buffered = BufferedReader(streamReader)
//
//                    val content = StringBuilder()
//                    while(true){
//                        val line = buffered.readLine() ?: break
//                        content.append(line)
//                    }
//
//                    buffered.close()
//                    urlConnection.disconnect()
//                    runOnUiThread {
//                        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//    }


}