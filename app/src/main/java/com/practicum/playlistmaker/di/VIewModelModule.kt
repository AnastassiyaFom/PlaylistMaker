package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.ui.viewModel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val viewModuleModule = module {

    factory {
        SearchViewModel (androidContext(),get(),get())
    }



}
