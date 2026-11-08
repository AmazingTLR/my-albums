package fr.leboncoin.feature.settings.di

import fr.leboncoin.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            observeUserDataUseCase = get(),
            setDarkThemeConfigUseCase = get(),
        )
    }
}
