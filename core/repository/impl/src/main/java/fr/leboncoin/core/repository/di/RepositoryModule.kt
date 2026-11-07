package fr.leboncoin.core.repository.di

import fr.leboncoin.core.common.dispatcher.AppDispatchers
import fr.leboncoin.core.repository.AlbumRepository
import fr.leboncoin.core.repository.AlbumRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<AlbumRepository> {
        AlbumRepositoryImpl(
            networkDataSource = get(),
            localDataSource = get(),
            dispatcher = get<CoroutineDispatcher>(named(AppDispatchers.IO)),
        )
    }
}
