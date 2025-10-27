package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitNetworkClient (private val trackSearchApiService: TrackSearchApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest){
            Response().apply { resultCode = 400 }
        }

        return withContext(context = Dispatchers.IO) {
            try {
                if (dto is TracksSearchRequest) {
                    val resp = trackSearchApiService.searchTracks(dto.expression)
                    resp.apply { resultCode = 200 }
                }
                else {
                    Response().apply { resultCode = 400 }
                }
            }
            catch(ex:Throwable) {
                    Response().apply { resultCode = 500 }
            }
        }

    }
}


