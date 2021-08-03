package com.summer.project


import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

data class ResponseDTO(var result:String? = null, var message:String?)

interface Service { //api 관리 인터페이스

    @FormUrlEncoded
    @POST("/google")
    fun GoogleAuth(@Field("token") token:String): Call<ResponseDTO>

    @FormUrlEncoded
    @POST("/kakao")
    fun KaKaoAuth(@Field("access_token") access_token:String, @Field("refresh_token") refresh_token:String): Call<ResponseDTO>

}