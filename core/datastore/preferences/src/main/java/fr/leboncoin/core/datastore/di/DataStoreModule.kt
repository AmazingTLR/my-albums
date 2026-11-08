package fr.leboncoin.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import fr.leboncoin.core.common.dispatcher.AppDispatchers
import fr.leboncoin.core.datastore.UserPreferencesDataSource
import fr.leboncoin.core.datastore.preferences.PreferencesUserPreferencesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val USER_PREFERENCES_NAME = "user_preferences"

val dataStoreModule = module {
    single<DataStore<Preferences>> {
        val context = androidContext()
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(AppDispatchers.IO))),
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_NAME) },
        )
    }
    single<UserPreferencesDataSource> { PreferencesUserPreferencesDataSource(dataStore = get()) }
}
