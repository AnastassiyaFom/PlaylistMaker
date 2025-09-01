package com.practicum.playlistmaker.search.data

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.Track

import java.util.Locale

class TracksRepositoryImpl (private val networkClient: NetworkClient): TracksRepository {
    override var resultCode = 0
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        resultCode = response.resultCode
        if (response.resultCode >= 200 && response.resultCode < 300) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    trackId = it.trackId?:0,
                    trackName = it.trackName?:"",
                    artistName = it.artistName?:"",
                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    artworkUrl100 = it.artworkUrl100?:"",
                    artworkUrl500 = enlargeImageUrl(it.artworkUrl100)?:"",
                    collectionName = it.collectionName?:"",
                    releaseDate = extractYear(it.releaseDate )?:"",
                    primaryGenreName = it.primaryGenreName?:"",
                    country = it.country?:"",
                    previewUrl = it.previewUrl?:""
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
    private fun extractYear(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Укажите формат вашей даты
        try {
            val date = dateFormat.parse(dateString)
            date?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it
                calendar[Calendar.YEAR]
            }
           val a= (date.getYear()+1900).toString()
            return a
        } catch (e: Exception) {
            return "" // Обработка ошибки, если строка не соответствует формату
        }

    }


}