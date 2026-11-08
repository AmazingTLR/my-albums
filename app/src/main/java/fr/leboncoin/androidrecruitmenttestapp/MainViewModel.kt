package fr.leboncoin.androidrecruitmenttestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.usecase.ObserveUserDataUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    observeUserDataUseCase: ObserveUserDataUseCase,
) : ViewModel() {

    // On failure the state stays Loading, i.e. the theme follows the system.
    val uiState: StateFlow<MainActivityUiState> = observeUserDataUseCase()
        .map<_, MainActivityUiState> { MainActivityUiState.Success(it) }
        .catch { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState.Loading,
        )
}
