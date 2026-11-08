package fr.leboncoin.core.usecase.di

import fr.leboncoin.core.usecase.GetAlbumGroupsUseCase
import fr.leboncoin.core.usecase.GetAlbumPhotosUseCase
import fr.leboncoin.core.usecase.ObserveFavoriteAlbumsUseCase
import fr.leboncoin.core.usecase.ObserveUserDataUseCase
import fr.leboncoin.core.usecase.RefreshAlbumsUseCase
import fr.leboncoin.core.usecase.SetDarkThemeConfigUseCase
import fr.leboncoin.core.usecase.ToggleAlbumFavoriteUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetAlbumGroupsUseCase> { GetAlbumGroupsUseCase(albumRepository = get()) }
    factory<RefreshAlbumsUseCase> { RefreshAlbumsUseCase(albumRepository = get()) }
    factory<GetAlbumPhotosUseCase> { GetAlbumPhotosUseCase(albumRepository = get()) }
    factory<ObserveFavoriteAlbumsUseCase> { ObserveFavoriteAlbumsUseCase(albumRepository = get()) }
    factory<ToggleAlbumFavoriteUseCase> { ToggleAlbumFavoriteUseCase(albumRepository = get()) }
    factory<ObserveUserDataUseCase> { ObserveUserDataUseCase(userDataRepository = get()) }
    factory<SetDarkThemeConfigUseCase> { SetDarkThemeConfigUseCase(userDataRepository = get()) }
}
