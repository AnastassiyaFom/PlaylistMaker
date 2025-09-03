package com.practicum.playlistmaker.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModuleModule
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private var darkTheme = false

    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {

      super.onCreate()
      startKoin {
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@App)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(dataModule,interactorModule,repositoryModule,viewModuleModule)
      }

      settingsInteractor = provideSettingsInteractor(this)

      if (settingsInteractor!=null) {
            darkTheme = settingsInteractor.getDarkThemeFlag()
            switchTheme(darkTheme)
        }
    }
    fun getDarkThemeFlag():Boolean{
        return darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        settingsInteractor.saveThemeMode(darkTheme)
    }

}
