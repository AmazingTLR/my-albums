package fr.leboncoin.feature.settings

import fr.leboncoin.core.model.DarkThemeConfig

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val darkThemeConfig: DarkThemeConfig) : SettingsUiState
    data object LoadFailed : SettingsUiState
}
