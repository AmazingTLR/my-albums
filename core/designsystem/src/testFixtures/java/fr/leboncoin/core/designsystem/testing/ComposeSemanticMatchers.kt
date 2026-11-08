package fr.leboncoin.core.designsystem.testing

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.test.core.app.ApplicationProvider

// Matches nodes whose text equals the resolved string resource
fun hasLocalizedText(
    @StringRes resId: Int,
    vararg formatArgs: Any,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
): SemanticsMatcher {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val text = if (formatArgs.isEmpty()) context.getString(resId) else context.getString(resId, *formatArgs)
    return hasText(text = text, substring = substring, ignoreCase = ignoreCase)
}

// Matches nodes whose text equals the resolved plural resource for [quantity]
fun hasLocalizedQuantityText(
    @PluralsRes resId: Int,
    quantity: Int,
    vararg formatArgs: Any,
): SemanticsMatcher {
    val context = ApplicationProvider.getApplicationContext<Context>()
    return hasText(context.resources.getQuantityString(resId, quantity, *formatArgs))
}

// Matches nodes whose content description equals the resolved string resource
fun hasLocalizedContentDescription(
    @StringRes resId: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
): SemanticsMatcher {
    val context = ApplicationProvider.getApplicationContext<Context>()
    return hasContentDescription(
        value = context.getString(resId),
        substring = substring,
        ignoreCase = ignoreCase,
    )
}
