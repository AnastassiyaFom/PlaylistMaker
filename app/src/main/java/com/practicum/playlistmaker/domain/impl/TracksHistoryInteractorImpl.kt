package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interfaces.interactors.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.TracksHistoryRepository
import com.practicum.playlistmaker.domain.models.Track


class TracksHistoryRepositoryImpl(private var searchHistory: TracksHistoryRepository):
    TracksHistoryInteractor {

    override fun getTracksFromHistory(): MutableList<Track> {
        return searchHistory.tracksInHistory
    }
    override fun getTracksInHistoryMaxLength(): Int{
        return searchHistory.tracksInHistoryMaxLength
    }

    override fun clearHistory() {
        searchHistory.clearHistory()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistory.loadHistory()
        //Меняем внутренний вспомагательный список
        if (searchHistory.tracksInHistory.contains(track) ){
            if (searchHistory.tracksInHistory.indexOf(track)>0)  {
                searchHistory.tracksInHistory.remove(track)
                searchHistory.tracksInHistory.add(0,track)
            }
            // если добавляемый трек уже стоит в начале списка, то ничего не делаем и выходим
            else return
        }
        else {
            if (searchHistory.tracksInHistory.size == searchHistory.tracksInHistoryMaxLength){
                searchHistory.tracksInHistory.removeAt(searchHistory.tracksInHistoryMaxLength-1)
            }
            searchHistory.tracksInHistory.add(0,track)
        }
        // Обновляем файл SharedPreferences
        searchHistory.refrashHistoryInRepository()
    }


}

