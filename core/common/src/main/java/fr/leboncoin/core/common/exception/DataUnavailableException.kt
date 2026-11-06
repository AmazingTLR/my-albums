package fr.leboncoin.core.common.exception

/**
 * Thrown when the requested data cannot be retrieved from its source.
 */
class DataUnavailableException(
    message: String = "The requested data is unavailable.",
    errorCode: String? = null,
    cause: Throwable? = null,
) : AppException(message, errorCode, cause)
