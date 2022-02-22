package com.task.favorite.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.task.core.domain.usecase.UserUseCase

class FavoriteViewModel(userUseCase: UserUseCase) : ViewModel() {
    val favoriteUsers = userUseCase.getFavoriteUser().asLiveData()
}