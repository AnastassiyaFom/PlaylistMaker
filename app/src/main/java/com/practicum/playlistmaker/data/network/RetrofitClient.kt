package com.practicum.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val trackSearchApiService = retrofit.create(TrackSearchApi::class.java)

}