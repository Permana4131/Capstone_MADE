package com.task.favorite.di

import com.task.favorite.favorite.FavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module{
    viewModel{FavoriteViewModel(get())}
}