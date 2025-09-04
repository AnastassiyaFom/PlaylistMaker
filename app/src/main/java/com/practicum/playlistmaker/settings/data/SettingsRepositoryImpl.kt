package com.practicum.playlistmaker.settings.data

import com.practicum.playlistmaker.search.data.PrefsStorageClient
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl( private var sharedPrefsClient: PrefsStorageClient<Boolean>): SettingsRepository {
    private var darkThemeFlag = false
    init {
        darkThemeFlag = sharedPrefsClient.getData()==true
    }

    override fun getDarkThemeFlag(): Boolean {
        return darkThemeFlag
    }
    override fun saveThemeMode(darkTheme:Boolean){
        sharedPrefsClient.storeData(darkTheme)
    }
}