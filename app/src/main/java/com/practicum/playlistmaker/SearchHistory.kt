package com.practicum.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.SearchActivity.Companion.TRACKS_HISTORY_PREFERENCES

class SearchHistory (var sharedPrefs: SharedPreferences){

    val tracksInHistoryMaxLength: Int = 10
    val tracksInHistory: MutableList<Track> = mutableListOf()
    private val TRACKS_HISTORY = "Tracks History"

    init{
        loadHistory()
       // sharedPrefs.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            // логика
       // }
    }

    private fun loadHistory(){
        tracksInHistory.clear()
        val json:String? = if (sharedPrefs==null) null else sharedPrefs.getString(TRACKS_HISTORY, "")
        if (!json.isNullOrEmpty()) {
            tracksInHistory.addAll(0,createTracksListFromJson(json))
        }
    }

    private fun refrashHistoryInSharedPrefs(){
        sharedPrefs.edit()
            .remove(TRACKS_HISTORY)
            .putString(TRACKS_HISTORY, createJsonFromTracksList())
            .apply()
    }

    fun clearHistory(){
        tracksInHistory.clear()
        sharedPrefs.edit()
                   .remove(TRACKS_HISTORY)
                   .apply()
    }

    fun getTracksFromHistory(): MutableList<Track>{
        return tracksInHistory
    }

    fun addTrackToHistory(track:Track){
        loadHistory()
        //Меняем внутренний вспомагательный список
        if (tracksInHistory.contains(track) ){
            if (tracksInHistory.indexOf(track)>0)  {
                tracksInHistory.remove(track)
                tracksInHistory.add(0,track)
            }
            // если добавляемый трек уже стоит в начале списка, то ничего не делаем и выходим
            else return
        }
        else {
            if (tracksInHistory.size == tracksInHistoryMaxLength){
                tracksInHistory.removeAt(tracksInHistoryMaxLength-1)
            }
            tracksInHistory.add(0,track)
        }
        // Обновляем файл SharedPreferences
        refrashHistoryInSharedPrefs()
    }

    private fun createJsonFromTracksList(): String {
        return Gson().toJson(tracksInHistory)
    }

    private fun createTracksListFromJson(json: String): MutableList<Track> {
        return Gson().fromJson(json, object : TypeToken<MutableList<Track>>() {}.type )
    }


}