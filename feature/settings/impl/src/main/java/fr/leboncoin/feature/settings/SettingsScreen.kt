package fr.leboncoin.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.leboncoin.core.designsystem.component.AppRadioButton
import fr.leboncoin.core.designsystem.component.AppScaffold
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.component.AppText
import fr.leboncoin.core.designsystem.screen.ErrorScreen
import fr.leboncoin.core.designsystem.screen.LoadingScreen
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.ui.LocalAppSnackbarHostState
import fr.leboncoin.core.ui.analytics.TrackScreenViewEvent
import fr.leboncoin.feature.settings.impl.R
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenViewEvent("SettingsScreen")

    val snackbarHostState = LocalAppSnackbarHostState.current
    val saveFailedMessage = stringResource(R.string.feature_settings_impl_save_failed)
    LaunchedEffect(settingsViewModel, snackbarHostState) {
        settingsViewModel.saveFailed.collect { snackbarHostState.show(saveFailedMessage) }
    }

    SettingsScreen(
        uiState = uiState,
        onChangeDarkThemeConfig = settingsViewModel::updateDarkThemeConfig,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onChangeDarkThemeConfig: (DarkThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppScaffold(modifier = modifier) { contentPadding ->
        when (uiState) {
            SettingsUiState.Loading -> LoadingScreen(modifier = Modifier.padding(contentPadding))
            SettingsUiState.LoadFailed -> ErrorScreen(
                errorMessage = stringResource(R.string.feature_settings_impl_load_failed),
                modifier = Modifier.padding(contentPadding),
            )

            is SettingsUiState.Success -> Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(AppTheme.dimensions.spaceMedium),
            ) {
                AppText(
                    text = stringResource(R.string.feature_settings_impl_dark_mode_preference),
                    style = AppTheme.typography.body,
                    modifier = Modifier.padding(bottom = AppTheme.dimensions.spaceSmall),
                )
                DarkThemeConfig.entries.forEach { config ->
                    AppRadioButton(
                        text = stringResource(config.labelRes()),
                        selected = uiState.darkThemeConfig == config,
                        onClick = { onChangeDarkThemeConfig(config) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

private fun DarkThemeConfig.labelRes() = when (this) {
    DarkThemeConfig.FOLLOW_SYSTEM -> R.string.feature_settings_impl_dark_mode_config_system_default
    DarkThemeConfig.LIGHT -> R.string.feature_settings_impl_dark_mode_config_light
    DarkThemeConfig.DARK -> R.string.feature_settings_impl_dark_mode_config_dark
}

@ThemePreviews
@Composable
private fun SettingsScreenSuccessPreview() {
    AppTheme {
        AppSurface {
            SettingsScreen(
                uiState = SettingsUiState.Success(DarkThemeConfig.FOLLOW_SYSTEM),
                onChangeDarkThemeConfig = {},
            )
        }
    }
}
