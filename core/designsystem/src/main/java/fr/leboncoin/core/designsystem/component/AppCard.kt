package fr.leboncoin.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card

@Composable
fun AppCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card.Flat(modifier = modifier, onClick = onClick, colors = SparkTheme.colors.backgroundVariant, content = content)
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card.Flat(modifier = modifier, colors = SparkTheme.colors.backgroundVariant, content = content)
}
