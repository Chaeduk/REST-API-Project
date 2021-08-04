package com.summer.project


import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

data class ResponseDTO(var result:String? = null, var message:String?)
data class ResponseMSG(var message:String?)

interface Service { //api 관리 인터페이스

    @FormUrlEncoded
    @POST("/google")
    fun GoogleAuth(@Field("token") token:String): Call<ResponseDTO>

    @FormUrlEncoded
    @POST("/kakao")
    fun KaKaoAuth(@Field("access_token") access_token:String, @Field("refresh_token") refresh_token:String): Call<ResponseDTO>

    @GET("/doublecheck")
    fun DoubleCheck(@Query("id")id:String): Call<ResponseMSG>

    @FormUrlEncoded
    @POST("/signup")
    fun Signup(@Field("id") id:String, @Field("pwd") pwd:String): Call<ResponseMSG>

    @FormUrlEncoded
    @POST("/local")
    fun Signin(@Field("id") id:String, @Field("pwd") pwd:String): Call<ResponseDTO>


}