package com.tyris.pagingLazy

import android.app.Application
import com.tyris.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class PagingLazyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PagingLazyApp)
            workManagerFactory()
            modules(
                dataModule
            )
        }
    }
}