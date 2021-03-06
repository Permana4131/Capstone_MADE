package com.task.core.data.remote.network

import com.task.core.data.remote.response.ListUserResponse
import com.task.core.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") q: String?
    ): ListUserResponse

    @GET("users/{username}")
    suspend fun userDetail(
        @Path("username") username: String?
    ): UserResponse

    @GET("users/{username}/followers")
    suspend fun userFollower(
        @Path("username") username: String?
    ): List<UserResponse>

    @GET("users/{username}/following")
    suspend fun userFollowing(
        @Path("username") username: String?
    ): List<UserResponse>
}