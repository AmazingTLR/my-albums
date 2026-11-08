package fr.leboncoin.feature.details.di

import fr.leboncoin.feature.details.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    viewModel<DetailsViewModel> { params ->
        DetailsViewModel(
            albumId = params.get(),
            getAlbumPhotosUseCase = get(),
            toggleAlbumFavoriteUseCase = get(),
        )
    }
}
