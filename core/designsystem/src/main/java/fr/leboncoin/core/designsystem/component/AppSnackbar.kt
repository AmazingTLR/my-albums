package fr.leboncoin.core.designsystem.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.components.snackbars.SnackbarIntent

@Stable
class AppSnackbarHostState {
    internal val spark = SnackbarHostState()

    suspend fun show(message: String) {
        spark.showSnackbar(message = message, intent = SnackbarIntent.Error, duration = SnackbarDuration.Short)
    }

    suspend fun showIndefinitely(message: String) {
        spark.showSnackbar(message = message, intent = SnackbarIntent.Error, duration = SnackbarDuration.Indefinite)
    }
}

@Composable
fun AppSnackbarHost(
    state: AppSnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(hostState = state.spark, modifier = modifier)
}
