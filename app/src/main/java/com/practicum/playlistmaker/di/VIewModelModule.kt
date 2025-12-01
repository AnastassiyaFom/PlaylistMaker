package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistAddingViewModel
import com.practicum.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistsViewModel
import com.practicum.playlistmaker.library.ui.viewModel.SelectedTracksViewModel
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val viewModuleModule = module {

    factory {
        SearchViewModel (get(),get())
    }

    factory{
        PlayerViewModel (get(), get(),get())
    }
    factory{
        SettingsViewModel(get(),get())
    }
    factory{
        MediaPlayer()
    }
    factory{
        PlaylistsViewModel(get())
    }
    factory{
        PlaylistAddingViewModel(get(),get(),get())
    }
    factory{
        SelectedTracksViewModel(get())
    }

    factory{
        PlaylistScreenViewModel(get(),androidContext(),get())
    }

}
