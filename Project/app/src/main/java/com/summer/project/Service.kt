package com.summer.project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class ResponseDTO(var result:String? = null)

interface Service { //api 관리 인터페이스

    @GET("/")
    fun getRequest(@Query("name") name:String): Call<ResponseDTO>

}