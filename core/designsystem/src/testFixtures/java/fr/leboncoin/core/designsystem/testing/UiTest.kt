package fr.leboncoin.core.designsystem.testing

import androidx.compose.ui.test.junit4.v2.createComposeRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Base class of every Compose UI test.
 *
 * It pins the runner, the locale and the SDK in one place. Change them here, never per class.
 */
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "en", sdk = [36])
abstract class UiTest {

    @get:Rule
    val composeTestRule = createComposeRule()
}
