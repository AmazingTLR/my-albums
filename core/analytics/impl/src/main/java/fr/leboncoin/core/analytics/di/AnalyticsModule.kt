package fr.leboncoin.core.analytics.di

import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.analytics.StubAnalyticsLogger
import org.koin.dsl.module

fun createAnalyticsModule(enableAnalyticsLogging: Boolean) = module {
    single<AnalyticsLogger> { StubAnalyticsLogger(enableLogging = enableAnalyticsLogging) }
}
