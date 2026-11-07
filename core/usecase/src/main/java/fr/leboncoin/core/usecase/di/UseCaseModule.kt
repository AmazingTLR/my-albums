package fr.leboncoin.core.usecase.di

import fr.leboncoin.core.usecase.GetAlbumGroupsUseCase
import fr.leboncoin.core.usecase.GetAlbumPhotosUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetAlbumGroupsUseCase> { GetAlbumGroupsUseCase(albumRepository = get()) }
    factory<GetAlbumPhotosUseCase> { GetAlbumPhotosUseCase(albumRepository = get()) }
}
