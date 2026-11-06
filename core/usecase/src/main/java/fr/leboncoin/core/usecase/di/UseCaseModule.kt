package fr.leboncoin.core.usecase.di

import fr.leboncoin.core.usecase.GetAlbumsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetAlbumsUseCase> { GetAlbumsUseCase(albumRepository = get()) }
}
