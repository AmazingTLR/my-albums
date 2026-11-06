package fr.leboncoin.core.ui.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.analytics.NoOpAnalyticsLogger
import fr.leboncoin.core.analytics.ParamKeys
import fr.leboncoin.core.analytics.Type

/**
 * Gives composables access to the [AnalyticsLogger]. Defaults to a no-op so tests and previews
 * do not have to provide one; the app provides the real implementation.
 */
val LocalAnalyticsLogger = staticCompositionLocalOf<AnalyticsLogger> { NoOpAnalyticsLogger() }

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsLogger: AnalyticsLogger = LocalAnalyticsLogger.current,
) = DisposableEffect(Unit) {
    analyticsLogger.logEvent(
        AnalyticsEvent(
            type = ScreenAnalyticsType.ScreenView,
            extras = listOf(AnalyticsEvent.Param(ScreenAnalyticsParamKeys.ScreenName, screenName)),
        ),
    )
    onDispose {}
}

private object ScreenAnalyticsType {
    object ScreenView : Type("screen_view")
}

private object ScreenAnalyticsParamKeys {
    object ScreenName : ParamKeys("screen_name")
}
