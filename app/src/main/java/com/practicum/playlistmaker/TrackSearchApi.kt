package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface TrackSearchApi {

    @POST("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TracksSearchResponse>
}