package com.practicum.playlistmaker.data.Repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.SearchHistoryDto
import com.practicum.playlistmaker.domain.api.TracksHistoryRepository
import com.practicum.playlistmaker.domain.models.Track


class TracksHistoryRepositorySharedPrefImpl(var context: Context):TracksHistoryRepository {
    //private val tracksInHistory: MutableList<Track> = mutableListOf()
   // private val tracksInHistoryMaxLength: Int = 10
    private val searchHistoryDto= SearchHistoryDto(mutableListOf(),10)
    private val TRACKS_HISTORY = "Tracks History"
    private val  sharedPrefs: SharedPreferences = context.getSharedPreferences("Tracks History Preferences", MODE_PRIVATE)
    init{
        loadHistory()
    }

    override fun getTracksFromHistory(): MutableList<Track>{
        return searchHistoryDto.tracksInHistory
    }
    override fun getTracksInHistoryMaxLength(): Int{
        return searchHistoryDto.tracksInHistoryMaxLength
    }

    override fun loadHistory() {
        searchHistoryDto.tracksInHistory.clear()
        val json:String? = if (sharedPrefs==null) null else sharedPrefs.getString(TRACKS_HISTORY, "")
        if (!json.isNullOrEmpty()) {
            searchHistoryDto.tracksInHistory.addAll(0,createTracksListFromJson(json))
        }
    }

    override fun refrashHistoryInRepository() {
        sharedPrefs.edit()
            .remove(TRACKS_HISTORY)
            .putString(TRACKS_HISTORY, createJsonFromTracksList())
            .apply()
    }

    override fun clearHistory() {
        searchHistoryDto.tracksInHistory.clear()
        sharedPrefs.edit()
            .remove(TRACKS_HISTORY)
            .apply()
    }

    override fun addTrackToHistory(track: Track) {
        loadHistory()
        //Меняем внутренний вспомагательный список
        if (searchHistoryDto.tracksInHistory.contains(track) ){
            if (searchHistoryDto.tracksInHistory.indexOf(track)>0)  {
                searchHistoryDto.tracksInHistory.remove(track)
                searchHistoryDto.tracksInHistory.add(0,track)
            }
            // если добавляемый трек уже стоит в начале списка, то ничего не делаем и выходим
            else return
        }
        else {
            if (searchHistoryDto.tracksInHistory.size == searchHistoryDto.tracksInHistoryMaxLength){
                searchHistoryDto.tracksInHistory.removeAt(searchHistoryDto.tracksInHistoryMaxLength-1)
            }
            searchHistoryDto.tracksInHistory.add(0,track)
        }
        // Обновляем файл SharedPreferences
        refrashHistoryInRepository()
    }

    private fun createJsonFromTracksList(): String {
        return Gson().toJson(searchHistoryDto.tracksInHistory)
    }

    private fun createTracksListFromJson(json: String): MutableList<Track> {
        return Gson().fromJson(json, object : TypeToken<MutableList<Track>>() {}.type )
    }
}

