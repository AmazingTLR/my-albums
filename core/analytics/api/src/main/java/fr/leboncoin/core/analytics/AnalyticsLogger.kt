package fr.leboncoin.core.analytics

interface AnalyticsLogger {
    fun logEvent(event: AnalyticsEvent)
}
