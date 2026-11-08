package fr.leboncoin.androidrecruitmenttestapp

import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData

sealed interface MainActivityUiState {

    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean
    data object Loading : MainActivityUiState {
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
    }

    data class Success(val userData: UserData) : MainActivityUiState {
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = when (userData.darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }
}
