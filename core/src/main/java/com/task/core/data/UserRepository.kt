package com.task.core.data


import com.task.core.data.remote.RemoteDataSource
import com.task.core.data.remote.network.ApiResponse
import com.task.core.data.remote.response.UserResponse
import com.task.core.domain.model.User
import com.task.core.domain.repository.IUserRepository
import com.task.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository  constructor(
    private  val remoteDataSource: RemoteDataSource,
    private val localDataSource: com.task.core.data.local.LocalDataSource,
): IUserRepository {
    override fun getAllUsers(query: String?): Flow<Resource<List<User>>> {
        return object : com.task.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getAllUsers(query)
            }

        }.asFlow()
    }


    override fun getAllFollowers(username: String): Flow<Resource<List<User>>> {
        return object : com.task.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getAllFollowers(username)
            }

        }.asFlow()
    }

    override fun getAllFollowing(username: String): Flow<Resource<List<User>>> {
        return object : com.task.core.data.NetworkBoundResource<List<User>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getAllFollowing(username)
            }
        }.asFlow()
    }

    override fun getDetailUser(username: String): Flow<Resource<User>> {
        return object : com.task.core.data.NetworkBoundResource<User, UserResponse>() {
            override fun loadFromNetwork(data: UserResponse): Flow<User> {
                return DataMapper.mapUserResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> {
                return remoteDataSource.getUserDetail(username)
            }

        }.asFlow()
    }

    override fun getFavoriteUser(): Flow<List<User>> {
        return localDataSource.getFavoriteUser().map{
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getDetailState(username: String): Flow<User>? {
        return localDataSource.getDetailState(username)?.map {
            DataMapper.mapEntityToDomain(it)
        }
    }

    override suspend fun insertUser(user: User) {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.insertUser(domainUser)
    }

    override suspend fun deleteUser(user: User): Int {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.deleteUser(domainUser)
    }
}
