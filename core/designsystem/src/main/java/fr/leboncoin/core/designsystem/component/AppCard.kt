package fr.leboncoin.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.card.Card

@OptIn(ExperimentalSparkApi::class)
@Composable
fun AppCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(modifier = modifier, onClick = onClick, content = content)
}
