@file:Suppress("unused")

package com.task.capstone_made

import android.app.Application
import com.task.capstone_made.di.useCaseModule
import com.task.capstone_made.di.viewModelModule
import com.task.core.di.databaseModule
import com.task.core.di.networkModule
import com.task.core.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}