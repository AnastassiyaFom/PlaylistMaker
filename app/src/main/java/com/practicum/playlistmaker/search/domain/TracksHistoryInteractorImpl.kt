package com.practicum.playlistmaker.search.domain


class TracksHistoryRepositoryImpl(private var searchHistoryRepository: SearchHistoryRepository):
    TracksHistoryInteractor {

    override fun getTracksFromHistory(): MutableList<Track> {
        return searchHistoryRepository.getTracksFromHistory()
    }
    override fun getTracksInHistoryMaxLength(): Int{
        return searchHistoryRepository.tracksInHistoryMaxLength
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }

    override fun addTrackToHistory(track: Track) {
        var tracksInHistory=searchHistoryRepository.getTracksFromHistory()

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
            if (tracksInHistory.size == searchHistoryRepository.tracksInHistoryMaxLength){
                tracksInHistory.removeAt(searchHistoryRepository.tracksInHistoryMaxLength-1)
            }
            tracksInHistory.add(0,track)
        }
        // Обновляем файл хранилища
        searchHistoryRepository.writeDataToRepository(tracksInHistory)
    }
}

