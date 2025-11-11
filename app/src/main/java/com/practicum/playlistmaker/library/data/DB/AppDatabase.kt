package com.practicum.playlistmaker.library.data.DB

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(version = 1, entities = [SelectedTrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun selectedTracksDao() : SelectedTracksDao

}

