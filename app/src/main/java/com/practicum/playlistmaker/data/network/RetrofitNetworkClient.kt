package com.practicum.playlistmaker.data.network


import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TracksSearchRequest


class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TracksSearchRequest) {
                val resp =
                    RetrofitClient.trackSearchApiService.searchTracks(dto.expression).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        }
        catch (ex:Exception) {
            return Response().apply { resultCode = 400 }
        }
    }

}


