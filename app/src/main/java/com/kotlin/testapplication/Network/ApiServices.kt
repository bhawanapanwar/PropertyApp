package com.kotlin.testapplication.Network

import com.google.gson.JsonElement


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


interface ApiServices {

    @Headers("Content-Type: application/json")
    @GET("/test/properties")
    fun property(): Call<JsonElement>
}