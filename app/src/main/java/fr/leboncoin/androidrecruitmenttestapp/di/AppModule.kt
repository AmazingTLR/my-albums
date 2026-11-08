package fr.leboncoin.androidrecruitmenttestapp.di

import fr.leboncoin.androidrecruitmenttestapp.MainViewModel
import fr.leboncoin.androidrecruitmenttestapp.util.ConnectivityManagerNetworkMonitor
import fr.leboncoin.androidrecruitmenttestapp.util.NetworkMonitor
import fr.leboncoin.core.common.dispatcher.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> { MainViewModel(observeUserDataUseCase = get()) }
    single<NetworkMonitor> {
        ConnectivityManagerNetworkMonitor(
            context = androidContext(),
            ioDispatcher = get<CoroutineDispatcher>(named(AppDispatchers.IO)),
        )
    }
}
