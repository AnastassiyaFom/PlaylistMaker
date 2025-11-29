package com.practicum.playlistmaker.library.data.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TracksDao  {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE  FROM tracks_table WHERE trackId = :id ")
    suspend fun deleteTrackById(id:Int)

    @Query("SELECT * FROM tracks_table WHERE trackId = :id ")
    suspend fun getTrackById(id:Int): TrackEntity?


    @Query("SELECT * FROM tracks_table WHERE trackId IN (:trackIds) ORDER BY dateAdded DESC")
    suspend fun getTracksByIds(trackIds: List<Int>): List<TrackEntity>

}
