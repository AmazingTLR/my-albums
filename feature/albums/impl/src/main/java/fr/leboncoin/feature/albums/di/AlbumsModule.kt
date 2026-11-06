package fr.leboncoin.feature.albums.di

import fr.leboncoin.feature.albums.AlbumsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val albumsModule = module {
    viewModel<AlbumsViewModel> {
        AlbumsViewModel(
            getAlbumsUseCase = get(),
            analyticsLogger = get(),
        )
    }
}
