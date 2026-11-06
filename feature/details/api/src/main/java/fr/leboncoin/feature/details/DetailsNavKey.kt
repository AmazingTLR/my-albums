package fr.leboncoin.feature.details

import androidx.navigation3.runtime.NavKey
import fr.leboncoin.core.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class DetailsNavKey(val albumId: Int) : NavKey

fun Navigator.navigateToDetails(albumId: Int) {
    navigate(DetailsNavKey(albumId))
}
