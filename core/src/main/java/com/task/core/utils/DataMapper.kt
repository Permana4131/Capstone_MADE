package com.task.core.utils

import com.task.core.data.local.entity.UserEntity
import com.task.core.data.remote.response.UserResponse
import com.task.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapResponsesToDomain(input: List<UserResponse>): Flow<List<User>>{
        val userList = ArrayList<User>()
        input.map { users->
            val user = User(
                id = users.id,
                login = users.login,
                url = users.url,
                avatarUrl = users.avatarUrl,
                name = users.name,
                location = users.location,
                publicRepos = users.publicRepos,
                type = users.type,
                followers = users.followers,
                following = users.following,
                isFavorite = false
            )
            userList.add(user)
        }
        return flowOf(userList)
    }

    fun mapUserResponseToDomain(user: UserResponse): Flow<User>{
        return flowOf(
            User(
                id = user.id,
                login = user.login,
                url = user.url,
                avatarUrl = user.avatarUrl,
                name = user.name,
                location = user.location,
                publicRepos = user.publicRepos,
                type = user.type,
                followers = user.followers,
                following = user.following,
                isFavorite = false
            )
        )
    }

    fun mapEntitiesToDomain(input: List<UserEntity>): List<User> =
        input.map { userEntity->
            User(
                id = userEntity.id,
                login = userEntity.login,
                url = userEntity.url,
                avatarUrl = userEntity.avatarUrl,
                name = userEntity.name,
                location = userEntity.location,
                publicRepos = userEntity.publicRepos,
                type = userEntity.type,
                followers = userEntity.followers,
                following = userEntity.following,
                isFavorite = userEntity.isFavorite
            )
        }

    fun mapEntityToDomain(userEntity: UserEntity?): User =
            User(
                id = userEntity?.id,
                login = userEntity?.login,
                url = userEntity?.url,
                avatarUrl = userEntity?.avatarUrl,
                name = userEntity?.name,
                location = userEntity?.location,
                publicRepos = userEntity?.publicRepos,
                type = userEntity?.type,
                followers = userEntity?.followers,
                following = userEntity?.following,
                isFavorite = userEntity?.isFavorite
            )


    fun mapDomainToEntity(input: User) =  UserEntity(
        id = input.id,
        login = input.login,
        url = input.url,
        avatarUrl = input.avatarUrl,
        name = input.name,
        location = input.location,
        publicRepos = input.publicRepos,
        type = input.type,
        followers = input.followers,
        following = input.following,
        isFavorite = input.isFavorite
    )
}