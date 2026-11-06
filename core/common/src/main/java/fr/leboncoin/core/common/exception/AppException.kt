package fr.leboncoin.core.common.exception

/**
 * The root exception for all expected application failures.
 *
 * @param message A human-readable description of the error for debugging purposes.
 * @param errorCode An optional diagnostic code for observability and logging.
 * @param cause The original exception that triggered this failure, used to preserve the stack trace.
 */
sealed class AppException(
    message: String,
    val errorCode: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)
