package fr.leboncoin.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.common.exception.AppException
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.usecase.ObserveUserDataUseCase
import fr.leboncoin.core.usecase.SetDarkThemeConfigUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    observeUserDataUseCase: ObserveUserDataUseCase,
    private val setDarkThemeConfigUseCase: SetDarkThemeConfigUseCase,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        observeUserDataUseCase()
            .map<_, SettingsUiState> { SettingsUiState.Success(it.darkThemeConfig) }
            .catch { emit(SettingsUiState.LoadFailed) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState.Loading,
            )

    private val saveFailures = Channel<Unit>(Channel.BUFFERED)

    // Failure to save is notified to user
    val saveFailed: Flow<Unit> = saveFailures.receiveAsFlow()

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            try {
                setDarkThemeConfigUseCase(darkThemeConfig)
            } catch (_: AppException) {
                saveFailures.send(Unit)
            }
        }
    }
}
