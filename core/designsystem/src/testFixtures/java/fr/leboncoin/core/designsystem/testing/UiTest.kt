package fr.leboncoin.core.designsystem.testing

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Base class of every Compose UI test, behavioral or screenshot.
 *
 * It pins the runner, the graphics mode (NATIVE is required by Roborazzi), the locale, the device
 * and the SDK in one place so screenshots stay deterministic.
 **
 * Record reference screenshots: `./gradlew :<module>:recordRoborazziDebug`.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "en-" + RobolectricDeviceQualifiers.Pixel7, sdk = [36])
abstract class UiTest {

    @get:Rule
    val composeTestRule = createComposeRule()
}
