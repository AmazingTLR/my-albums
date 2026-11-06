package fr.leboncoin.core.common.exception

/**
 * Thrown when data is received but cannot be parsed or interpreted.
 */
class DataCorruptedException(
    message: String = "The data is corrupted or unreadable.",
    errorCode: String? = null,
    cause: Throwable? = null,
) : AppException(message, errorCode, cause)
