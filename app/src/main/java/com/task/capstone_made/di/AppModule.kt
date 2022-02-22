package com.task.capstone_made.di

import com.task.capstone_made.ui.detail.DetailViewModel
import com.task.capstone_made.ui.follow.FollowViewModel
import com.task.capstone_made.ui.home.HomeViewModel
import com.task.core.domain.usecase.UserInteractor
import com.task.core.domain.usecase.UserUseCase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase>{ UserInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { FollowViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}