package fr.leboncoin.feature.favorites.di

import fr.leboncoin.feature.favorites.FavoritesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoritesModule = module {
    viewModel<FavoritesViewModel> {
        FavoritesViewModel(
            observeFavoriteAlbumsUseCase = get(),
            toggleAlbumFavoriteUseCase = get(),
        )
    }
}
