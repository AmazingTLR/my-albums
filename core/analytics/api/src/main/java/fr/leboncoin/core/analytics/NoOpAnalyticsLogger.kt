package fr.leboncoin.core.analytics

/** Default logger for tests and previews: drops every event. */
class NoOpAnalyticsLogger : AnalyticsLogger {
    override fun logEvent(event: AnalyticsEvent) = Unit
}
