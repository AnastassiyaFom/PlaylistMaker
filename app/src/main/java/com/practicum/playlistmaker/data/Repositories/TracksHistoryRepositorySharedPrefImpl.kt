package com.practicum.playlistmaker.data.Repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.SearchHistoryDto
import com.practicum.playlistmaker.domain.interfaces.repositories.TracksHistoryRepository
import com.practicum.playlistmaker.domain.models.Track


class TracksHistoryRepositorySharedPrefImpl(var context: Context): TracksHistoryRepository {
    override val tracksInHistoryMaxLength: Int = 10
    override var tracksInHistory: MutableList<Track>
    private val TRACKS_HISTORY = "Tracks History"
    private val  sharedPrefs: SharedPreferences = context.getSharedPreferences("Tracks History Preferences", MODE_PRIVATE)

    init{
        tracksInHistory = loadHistory()
    }

    // Явно кажется лишним введение  searchHistoryDto, но в задании указано, что должен быть Dto
    override fun loadHistory(): MutableList<Track> {
        val searchHistoryDto = SearchHistoryDto()

        val json:String? = if (sharedPrefs==null) null else sharedPrefs.getString(TRACKS_HISTORY, "")
        if (!json.isNullOrEmpty()) {
            searchHistoryDto.tracksInHistory.addAll(0,createTracksListFromJson(json))
        }
        return searchHistoryDto.tracksInHistory
    }

    override fun refrashHistoryInRepository() {
        sharedPrefs.edit()
            .remove(TRACKS_HISTORY)
            .putString(TRACKS_HISTORY, createJsonFromTracksList())
            .apply()
    }

    override fun clearHistory() {
        tracksInHistory.clear()
        sharedPrefs.edit()
            .remove(TRACKS_HISTORY)
            .apply()
    }


    private fun createJsonFromTracksList(): String {
        return Gson().toJson(tracksInHistory)
    }

    private fun createTracksListFromJson(json: String): MutableList<Track> {
        return Gson().fromJson(json, object : TypeToken<MutableList<Track>>() {}.type )
    }
}

