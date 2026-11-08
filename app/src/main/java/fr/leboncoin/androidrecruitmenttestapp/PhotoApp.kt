package fr.leboncoin.androidrecruitmenttestapp

import android.app.Application
import fr.leboncoin.androidrecruitmenttestapp.di.appModule
import fr.leboncoin.core.analytics.di.createAnalyticsModule
import fr.leboncoin.core.common.dispatcher.di.dispatchersModule
import fr.leboncoin.core.database.di.databaseModule
import fr.leboncoin.core.network.di.createNetworkModule
import fr.leboncoin.core.repository.di.repositoryModule
import fr.leboncoin.core.usecase.di.useCaseModule
import fr.leboncoin.feature.albums.di.albumsModule
import fr.leboncoin.feature.details.di.detailsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PhotoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@PhotoApp)
            modules(
                dispatchersModule,
                createAnalyticsModule(enableAnalyticsLogging = BuildConfig.DEBUG),
                createNetworkModule(enableHttpLogging = BuildConfig.DEBUG),
                databaseModule,
                repositoryModule,
                useCaseModule,
                albumsModule,
                detailsModule,
                appModule,
            )
        }
    }
}
