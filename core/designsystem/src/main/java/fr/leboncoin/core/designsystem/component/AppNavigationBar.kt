package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.appbar.NavigationBar
import com.adevinta.spark.components.appbar.NavigationBarItem
import com.adevinta.spark.components.text.Text

data class AppNavigationBarItem(
    val label: String,
    val icon: AppIcon,
    val selectedIcon: AppIcon,
    val selected: Boolean,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalSparkApi::class)
@Composable
fun AppNavigationBar(
    items: List<AppNavigationBarItem>,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.selected,
                onClick = item.onClick,
                icon = if (item.selected) item.selectedIcon.spark else item.icon.spark,
                label = { Text(text = item.label) },
            )
        }
    }
}
