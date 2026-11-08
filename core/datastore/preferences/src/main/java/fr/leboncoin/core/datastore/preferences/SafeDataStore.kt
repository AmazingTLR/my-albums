package fr.leboncoin.core.datastore.preferences

import fr.leboncoin.core.common.exception.DataUnavailableException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Translates storage failures into [DataUnavailableException].
 * [CancellationException] is always rethrown so structured concurrency keeps working.
 */
internal suspend fun <T> safeDataStoreCall(block: suspend () -> T): T =
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        throw DataUnavailableException(cause = e)
    }

internal fun <T> Flow<T>.safeDataStoreFlow(): Flow<T> = catch { e ->
    if (e is IOException) throw DataUnavailableException(cause = e) else throw e
}
