package com.practicum.playlistmaker.library.data.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(version = 1, entities = [SelectedTrackEntity::class])
//@TypeConverters(TrackDbConvertor::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun selectedTracksDao() : SelectedTracksDao

}

