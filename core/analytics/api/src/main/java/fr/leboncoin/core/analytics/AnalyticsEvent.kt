package fr.leboncoin.core.analytics

open class Type(val label: String)

open class ParamKeys(val label: String)

/**
 * Represents an analytics event.
 * @param extras - list of parameters which supply additional context to the event. See [Param].
 */
data class AnalyticsEvent(
    val type: Type,
    val extras: List<Param> = emptyList(),
) {
    /** A key-value pair used to supply extra context to an analytics event. */
    data class Param(val key: ParamKeys, val value: String)
}
