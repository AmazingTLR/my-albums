package fr.leboncoin.core.common.dispatcher.di

import fr.leboncoin.core.common.dispatcher.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatchersModule = module {
    single<CoroutineDispatcher>(named(AppDispatchers.Default)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(AppDispatchers.IO)) { Dispatchers.IO }
}
