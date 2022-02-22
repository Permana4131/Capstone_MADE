package com.task.capstone_made.ui.home

import androidx.lifecycle.*
import com.task.core.domain.model.User
import com.task.core.domain.usecase.UserUseCase

class HomeViewModel(userUseCase: UserUseCase) : ViewModel() {
    private var username: MutableLiveData<String> = MutableLiveData()

    fun setSearch(query: String) {
        if (username.value == query) {
            return
        }
        username.value = query
    }

    val users: LiveData<com.task.core.data.Resource<List<User>>> = Transformations
        .switchMap(username) {
            userUseCase.getAllUsers(it).asLiveData()
        }
}