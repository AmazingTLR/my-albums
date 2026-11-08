package fr.leboncoin.androidrecruitmenttestapp.ui

import app.cash.turbine.test
import fr.leboncoin.androidrecruitmenttestapp.util.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoAppStateTest {

    private class FakeNetworkMonitor : NetworkMonitor {
        val online = MutableStateFlow(true)
        override val isOnline: Flow<Boolean> = online
    }

    @Test
    fun `isOffline follows the network monitor`() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val networkMonitor = FakeNetworkMonitor()
        val sut = PhotoAppState(networkMonitor, backgroundScope)

        sut.isOffline.test {
            assertEquals(false, awaitItem())

            // When
            networkMonitor.online.value = false

            // Then
            assertEquals(true, awaitItem())

            // When
            networkMonitor.online.value = true

            // Then
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `isOffline is false before the first reading`() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val sut = PhotoAppState(NetworkMonitorNeverEmitting, backgroundScope)

        // When / Then
        assertEquals(false, sut.isOffline.value)
    }

    private object NetworkMonitorNeverEmitting : NetworkMonitor {
        override val isOnline: Flow<Boolean> = kotlinx.coroutines.flow.emptyFlow()
    }
}
