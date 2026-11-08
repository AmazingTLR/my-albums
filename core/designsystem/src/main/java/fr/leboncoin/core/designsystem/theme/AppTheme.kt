package fr.leboncoin.core.designsystem.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.darkSparkColors
import com.adevinta.spark.tokens.lightSparkColors

/** Entry point to the design tokens. Backed by Spark, but exposes only app-level types. */
object AppTheme {
    val dimensions: AppDimensions
        @Composable @ReadOnlyComposable
        get() = LocalDimensions.current

    val typography: AppTypography
        @Composable
        get() {
            val body = SparkTheme.typography.body1
            val caption = SparkTheme.typography.caption
            return remember(body, caption) { AppTypography(body = body, caption = caption) }
        }
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    SparkTheme(colors = if (darkTheme) darkSparkColors() else lightSparkColors()) {
        CompositionLocalProvider(
            LocalDimensions provides AppDimensions(),
            content = content,
        )
    }
}

/**
 * Multipreview annotation that represents light and dark themes. Add this annotation to a
 * composable to render both themes.
 */
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews
