package com.task.capstone_made.ui.detail

import androidx.lifecycle.*
import com.task.core.domain.model.User
import com.task.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    fun detailUsers(username: String) = userUseCase.getDetailUser(username).asLiveData()

    fun getDetailState(username: String) = userUseCase.getDetailState(username)?.asLiveData()

    fun insertFavorite(user: User) = viewModelScope.launch {
        userUseCase.insertUser(user)
    }

    fun deleteFavorite(user: User) = viewModelScope.launch {
        userUseCase.deleteUser(user)
    }

}