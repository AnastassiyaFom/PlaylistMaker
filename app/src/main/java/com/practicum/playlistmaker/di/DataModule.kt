package com.practicum.playlistmaker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.PrefsStorageClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.TrackSearchApi
import com.practicum.playlistmaker.search.domain.StorageClient
import com.practicum.playlistmaker.search.domain.Track
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single<TrackSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackSearchApi::class.java)
    }
    single(named("tracksHistoryPreferences" )){
        androidContext()
            .getSharedPreferences("Tracks History Preferences", MODE_PRIVATE)
    }
    single(named("checkedTrackPreferences" )){
        androidContext().getSharedPreferences("CHECKED_TRACK", MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
single<StorageClient <MutableList<Track>>>{
     PrefsStorageClient<MutableList<Track>>(
         get(),
         "Tracks History",
         object : TypeToken<MutableList<Track>>() {}.type)
    }


}
