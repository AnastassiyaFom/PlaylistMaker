package com.practicum.playlistmaker.library.data.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SelectedTracksDao  {

    @Insert(entity = SelectedTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: SelectedTrackEntity)

    @Delete (entity = SelectedTrackEntity::class)
    suspend fun deleteTrack(track: SelectedTrackEntity)


    @Query("SELECT * FROM selected_track_table ORDER BY dateAdded DESC")
    suspend fun getTracksSortedByDate(): List<SelectedTrackEntity>


    @Query("SELECT * FROM selected_track_table WHERE trackId = :id ")
    suspend fun getTrackById(id:Int): SelectedTrackEntity?

}
