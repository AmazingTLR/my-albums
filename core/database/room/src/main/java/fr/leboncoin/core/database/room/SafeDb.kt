package fr.leboncoin.core.database.room

import android.database.SQLException
import fr.leboncoin.core.common.exception.DataUnavailableException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Translates storage failures into [DataUnavailableException].
 * [CancellationException] is always rethrown so structured concurrency keeps working.
 */
internal suspend fun <T> safeDbCall(block: suspend () -> T): T =
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLException) {
        throw DataUnavailableException(cause = e)
    } catch (e: IllegalStateException) {
        throw DataUnavailableException(cause = e)
    }

internal fun <T> Flow<T>.safeDbFlow(): Flow<T> = catch { e ->
    when (e) {
        is SQLException, is IllegalStateException -> throw DataUnavailableException(cause = e)
        else -> throw e
    }
}
