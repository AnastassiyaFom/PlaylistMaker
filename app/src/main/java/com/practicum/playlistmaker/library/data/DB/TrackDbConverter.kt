package com.practicum.playlistmaker.library.data.DB

import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import java.util.Date

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
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

    fun map(track: TrackEntity?): Track {
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
