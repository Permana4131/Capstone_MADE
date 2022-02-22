package com.task.core.di

import androidx.room.Room
import com.task.core.BuildConfig.API_KEY
import com.task.core.BuildConfig.BASE_URL
import com.task.core.data.local.room.UserDatabase
import com.task.core.data.remote.RemoteDataSource
import com.task.core.data.remote.network.ApiService
import com.task.core.domain.repository.IUserRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<UserDatabase>().userDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            UserDatabase::class.java,"User.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", API_KEY)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module{
    single { com.task.core.data.local.LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IUserRepository> {
        com.task.core.data.UserRepository(
            get(),
            get()
        )
    }
}