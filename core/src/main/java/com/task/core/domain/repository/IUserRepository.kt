package com.task.core.domain.repository

import com.task.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getAllUsers(query: String?): Flow<com.task.core.data.Resource<List<User>>>

    fun getAllFollowers(username: String): Flow<com.task.core.data.Resource<List<User>>>

    fun getAllFollowing(username: String): Flow<com.task.core.data.Resource<List<User>>>

    fun getDetailUser(username: String): Flow<com.task.core.data.Resource<User>>

    fun getFavoriteUser(): Flow<List<User>>

    fun getDetailState(username: String): Flow<User>?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User): Int
}