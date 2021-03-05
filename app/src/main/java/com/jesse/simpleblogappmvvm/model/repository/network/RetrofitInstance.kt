package com.jesse.simpleblogappmvvm.model.repository.network

import com.jesse.simpleblogappmvvm.model.repository.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }

}