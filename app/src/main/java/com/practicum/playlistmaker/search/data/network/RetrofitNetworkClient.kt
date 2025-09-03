package com.practicum.playlistmaker.search.data.network


import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest


class RetrofitNetworkClient (private val trackSearchApiService: TrackSearchApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TracksSearchRequest) {
                val resp =
                    trackSearchApiService.searchTracks(dto.expression).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        }
        catch (ex:Exception) {
            return Response().apply { resultCode = 500 }
        }
    }

}


