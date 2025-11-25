/*
package com.practicum.playlistmaker.library.data

import com.practicum.playlistmaker.library.data.DB.AppDatabase
import com.practicum.playlistmaker.library.data.DB.SelectedTrackEntity
import com.practicum.playlistmaker.library.data.DB.TrackDbConvertor
import com.practicum.playlistmaker.library.domain.db.SelectedTracksRepository

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking


class SelectedTracksRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : SelectedTracksRepository {

    override fun selectedTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.selectedTracksDao().getTracksSortedByDate()
        emit(convertFromEntityToTrack(tracks))
    }

    private fun convertFromEntityToTrack(tracks: List<SelectedTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
    private fun convertFromEntityToTrack(track: SelectedTrackEntity): Track {
        return trackDbConvertor.map(track)
    }
    private fun convertFromTrackToEntity(track: Track):SelectedTrackEntity  {
        return trackDbConvertor.map(track)
    }

    override  fun insertTrack(track:Track) {
        runBlocking {
            appDatabase.selectedTracksDao().insertTrack(convertFromTrackToEntity(track))
        }
    }

    override fun deleteTrack(track:Track) {
        runBlocking {
            appDatabase.selectedTracksDao().deleteTrack(convertFromTrackToEntity(track))
        }
    }
    override fun getTrackById(id:Int):Track?{
        var track:SelectedTrackEntity?
        runBlocking {
             track = appDatabase.selectedTracksDao().getTrackById(id)
        }
        if (track == null) return null
        return convertFromEntityToTrack(track!!)
    }

}

 */

package com.practicum.playlistmaker.library.data


import com.practicum.playlistmaker.library.data.DB.SelectedTrackEntity
import com.practicum.playlistmaker.library.data.DB.SelectedTracksDao
import com.practicum.playlistmaker.library.data.DB.SelectedTrackDbConvertor
import com.practicum.playlistmaker.library.domain.db.SelectedTracksRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking


class SelectedTracksRepositoryImpl (
    private val appDatabase: SelectedTracksDao,
    private val trackDbConvertor: SelectedTrackDbConvertor,
) : SelectedTracksRepository {

    override fun getSelectedTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.getTracksSortedByDate()
        emit(convertFromEntityToTrack(tracks))
    }

    private fun convertFromEntityToTrack(tracks: List<SelectedTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
    private fun convertFromEntityToTrack(track: SelectedTrackEntity): Track {
        return trackDbConvertor.map(track)
    }
    private fun convertFromTrackToEntity(track: Track,isInFavorites:Boolean):SelectedTrackEntity  {
        return trackDbConvertor.map(track)
    }

    override  fun insertTrack(track:Track,isInFavorites:Boolean) {
        runBlocking {
            appDatabase.insertTrack(convertFromTrackToEntity(track,isInFavorites))
        }
    }

    override fun deleteTrackFromFavorites(track:Track) {
        runBlocking {

                appDatabase.deleteTrack(trackDbConvertor.map(track))
        }
    }
    override fun deleteTrackFromDb(track:Track) {
        runBlocking {
                appDatabase.deleteTrack(trackDbConvertor.map(track))

        }
    }

    override fun getTrackById(id:Int):Track?{
        var track:SelectedTrackEntity?
        runBlocking {
             track = appDatabase.getTrackById(id)
        }
        if (track == null) return null
        return convertFromEntityToTrack(track!!)
    }


}
