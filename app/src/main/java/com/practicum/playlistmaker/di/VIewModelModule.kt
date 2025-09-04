package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.ui.viewModel.LibraryViewModel
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.dsl.module


val viewModuleModule = module {

    factory {
        SearchViewModel (get(),get())
    }

    factory{
        LibraryViewModel (get())
    }
    factory{
        SettingsViewModel(get(),get())
    }

}
