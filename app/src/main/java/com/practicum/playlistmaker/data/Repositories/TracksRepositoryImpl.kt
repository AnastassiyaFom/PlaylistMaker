package com.practicum.playlistmaker.data.Repositories

import android.icu.text.SimpleDateFormat
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.interfaces.repositories.TracksRepository
import com.practicum.playlistmaker.domain.models.Track

import java.util.Locale

class TracksRepositoryImpl (private val networkClient: NetworkClient): TracksRepository {
    override var resultCode = 0
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        resultCode = response.resultCode
        if (response.resultCode >= 200 && response.resultCode < 300) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    artworkUrl100 = it.artworkUrl100,
                    artworkUrl500 = enlargeImageUrl(it.artworkUrl100),
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                ) }
        } else {
            return emptyList()
        }
    }
    private fun enlargeImageUrl(artworkUrl: String?): String {
        if (artworkUrl != null) {
            return artworkUrl.replaceAfterLast('/',"512x512bb.jpg")
        }
        return artworkUrl.toString()
    }


}