package com.summer.project


import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

data class ResponseDTO(var result:String? = null)

interface Service { //api 관리 인터페이스

    @FormUrlEncoded
    @POST("/")
    fun getRequest(@Field("name") name:String): Call<JsonElement>

}