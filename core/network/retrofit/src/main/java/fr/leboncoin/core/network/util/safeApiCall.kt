package fr.leboncoin.core.network.util

import fr.leboncoin.core.common.exception.DataCorruptedException
import fr.leboncoin.core.common.exception.DataUnavailableException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs a network call and translates library failures into [fr.leboncoin.core.common.exception.AppException]s.
 * [CancellationException] is always rethrown so structured concurrency keeps working.
 */
internal suspend fun <T> safeApiCall(block: suspend () -> T): T =
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        throw DataUnavailableException(errorCode = e.code().toString(), cause = e)
    } catch (e: SerializationException) {
        throw DataCorruptedException(cause = e)
    } catch (e: Exception) {
        throw DataUnavailableException(cause = e)
    }
