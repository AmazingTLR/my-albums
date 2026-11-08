package fr.leboncoin.androidrecruitmenttestapp.di

import fr.leboncoin.androidrecruitmenttestapp.util.ConnectivityManagerNetworkMonitor
import fr.leboncoin.androidrecruitmenttestapp.util.NetworkMonitor
import fr.leboncoin.core.common.dispatcher.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<NetworkMonitor> {
        ConnectivityManagerNetworkMonitor(
            context = androidContext(),
            ioDispatcher = get<CoroutineDispatcher>(named(AppDispatchers.IO)),
        )
    }
}
