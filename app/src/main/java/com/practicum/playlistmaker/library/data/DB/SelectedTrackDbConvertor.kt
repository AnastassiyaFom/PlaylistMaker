package com.practicum.playlistmaker.library.data.DB

import com.practicum.playlistmaker.search.domain.Track
import java.util.Date

class SelectedTrackDbConvertor {
    fun map(track: Track): SelectedTrackEntity {
        return SelectedTrackEntity(
            trackId=track.trackId,
            trackName=track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100=track.artworkUrl100,
            artworkUrl500=track.artworkUrl500,
            collectionName=track.collectionName,
            releaseDate=track.releaseDate,
            primaryGenreName=track.primaryGenreName,
            country=track.country,
            previewUrl=track.previewUrl,
            dateAdded = Date().time
        )

    }

    fun map(track: SelectedTrackEntity?):Track {
        return Track(
            trackId=track?.trackId?:-1,
            trackName=track?.trackName?:"",
            artistName = track?.artistName?:"",
            trackTime = track?.trackTime?:"",
            artworkUrl100=track?.artworkUrl100?:"",
            artworkUrl500=track?.artworkUrl500?:"",
            collectionName=track?.collectionName?:"",
            releaseDate=track?.releaseDate?:"",
            primaryGenreName=track?.primaryGenreName?:"",
            country=track?.country?:"",
            previewUrl=track?.previewUrl?:""
        )
    }
}
