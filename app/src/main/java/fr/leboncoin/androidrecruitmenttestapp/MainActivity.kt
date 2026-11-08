package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.PhotoNavigation
import fr.leboncoin.androidrecruitmenttestapp.ui.rememberPhotoAppState
import fr.leboncoin.androidrecruitmenttestapp.util.NetworkMonitor
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.ui.analytics.LocalAnalyticsLogger
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val analyticsLogger: AnalyticsLogger by inject()
    private val networkMonitor: NetworkMonitor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(LocalAnalyticsLogger provides analyticsLogger) {
                AppTheme {
                    PhotoNavigation(appState = rememberPhotoAppState(networkMonitor))
                }
            }
        }
    }
}
