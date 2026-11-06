package fr.leboncoin.core.analytics

import android.util.Log

private const val TAG = "StubAnalyticsLogger"

/** Logs analytics events to Logcat when [enableLogging] is true. Replace/augment with a real backend when one is available. */
class StubAnalyticsLogger(private val enableLogging: Boolean) : AnalyticsLogger {
    override fun logEvent(event: AnalyticsEvent) {
        if (enableLogging) {
            Log.d(TAG, "Received analytics event: $event")
        }
    }
}
