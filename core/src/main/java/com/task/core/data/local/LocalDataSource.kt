package com.task.core.data.local

import com.task.core.data.local.entity.UserEntity
import com.task.core.data.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource constructor(private val userDao: UserDao){
    fun getFavoriteUser(): Flow<List<UserEntity>> = userDao.getFavoriteUser()

    fun getDetailState(username: String): Flow<UserEntity>? = userDao.getDetailState(username)

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)
}