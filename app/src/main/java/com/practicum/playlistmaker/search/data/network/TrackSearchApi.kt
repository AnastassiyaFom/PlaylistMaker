package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse

import retrofit2.http.POST
import retrofit2.http.Query

interface TrackSearchApi {
    @POST("/search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): TracksSearchResponse

}