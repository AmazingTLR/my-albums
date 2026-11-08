package fr.leboncoin.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card

/** Click action of the enclosing clickable [AppCard], or null. Read by [AppChip] to forward taps. */
internal val LocalAppCardOnClick = staticCompositionLocalOf<(() -> Unit)?> { null }

@Composable
fun AppCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(LocalAppCardOnClick provides onClick) {
        Card.Flat(modifier = modifier, onClick = onClick, colors = SparkTheme.colors.backgroundVariant, content = content)
    }
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Not clickable: a chip must not forward taps to an outer card.
    CompositionLocalProvider(LocalAppCardOnClick provides null) {
        Card.Flat(modifier = modifier, colors = SparkTheme.colors.backgroundVariant, content = content)
    }
}
