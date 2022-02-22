package com.task.core.data.remote

import android.util.Log
import com.task.core.data.remote.network.ApiResponse
import com.task.core.data.remote.network.ApiService
import com.task.core.data.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource constructor(private val apiService: ApiService){
    fun getAllUsers(query: String?): Flow<ApiResponse<List<UserResponse>>> {
        return flow {
            try {
                val userSearch = apiService.searchUsers(query)
                val userArray = userSearch.items
                if (userArray.isEmpty()) {
                    emit(ApiResponse.Error(null))
                } else {
                    emit(ApiResponse.Success(userArray))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(RemoteDataSource::class.java.simpleName, e.toString() )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUserDetail(username: String): Flow<ApiResponse<UserResponse>> =
        flow {
            try {
                val userDetail = apiService.userDetail(username)
                emit(ApiResponse.Success(userDetail))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)


    suspend fun getAllFollowers(username: String): Flow<ApiResponse<List<UserResponse>>> =
        flow {
            try {
                val userFollower = apiService.userFollower(username)
                emit(ApiResponse.Success(userFollower))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getAllFollowing(username: String): Flow<ApiResponse<List<UserResponse>>> =
        flow {
            try {
                val userFollowing = apiService.userFollowing(username)
                emit(ApiResponse.Success(userFollowing))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
}