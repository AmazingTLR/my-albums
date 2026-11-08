package fr.leboncoin.androidrecruitmenttestapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import fr.leboncoin.androidrecruitmenttestapp.ui.PhotoNavigation
import fr.leboncoin.androidrecruitmenttestapp.ui.rememberPhotoAppState
import fr.leboncoin.androidrecruitmenttestapp.util.NetworkMonitor
import fr.leboncoin.androidrecruitmenttestapp.util.isSystemInDarkTheme
import fr.leboncoin.androidrecruitmenttestapp.util.systemDarkThemeFlow
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.ui.analytics.LocalAnalyticsLogger
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val analyticsLogger: AnalyticsLogger by inject()
    private val networkMonitor: NetworkMonitor by inject()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var darkTheme by mutableStateOf(resources.configuration.isSystemInDarkTheme)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(systemDarkThemeFlow(), viewModel.uiState) { systemDark, uiState ->
                    uiState.shouldUseDarkTheme(systemDark)
                }
                    .onEach { darkTheme = it }
                    .distinctUntilChanged()
                    // The default edge-to-edge style follows the system, not the user's choice.
                    .collect { isDark ->
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                lightScrim = Color.TRANSPARENT,
                                darkScrim = Color.TRANSPARENT,
                            ) { isDark },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                                darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                            ) { isDark },
                        )
                    }
            }
        }

        setContent {
            CompositionLocalProvider(LocalAnalyticsLogger provides analyticsLogger) {
                AppTheme(darkTheme = darkTheme) {
                    PhotoNavigation(appState = rememberPhotoAppState(networkMonitor))
                }
            }
        }
    }
}
