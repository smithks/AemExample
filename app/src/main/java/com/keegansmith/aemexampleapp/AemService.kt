package com.keegansmith.aemexampleapp

import retrofit2.Call
import retrofit2.http.GET


interface AemService {

    @GET("aem-middleware")
    fun getResponse(): Call<List<AemEntry>>;
}