package com.practicum.playlistmaker.main.data

import com.practicum.playlistmaker.search.data.dto.Response


interface NetworkClient {
    fun doRequest(dto: Any): Response
}