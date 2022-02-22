package com.task.capstone_made.ui.follow

import androidx.lifecycle.*
import com.task.core.domain.model.User
import com.task.core.domain.usecase.UserUseCase
import com.task.core.utils.TypeView

class FollowViewModel(userUseCase: UserUseCase) : ViewModel() {
    private var username: MutableLiveData<String> = MutableLiveData()
    private lateinit var typeView: TypeView

    fun setFollow(user: String, type: TypeView) {
        if (username.value == user) {
            return
        }
        username.value = user
        typeView = type
    }

    val favoriteUsers:LiveData<com.task.core.data.Resource<List<User>>> = Transformations
        .switchMap(username) {
            when (typeView) {
                TypeView.FOLLOWER -> userUseCase.getAllFollowers(it).asLiveData()
                TypeView.FOLLOWING -> userUseCase.getAllFollowing(it).asLiveData()
            }
        }
}