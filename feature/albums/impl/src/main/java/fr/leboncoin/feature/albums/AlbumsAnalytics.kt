package fr.leboncoin.feature.albums

import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.analytics.ParamKeys
import fr.leboncoin.core.analytics.Type

internal fun AnalyticsLogger.logEventAlbumSelected(albumId: Int) {
    logEvent(
        AnalyticsEvent(
            type = AlbumsAnalyticsType.SelectItem,
            extras = listOf(AnalyticsEvent.Param(AlbumsAnalyticsParamKeys.ItemId, albumId.toString())),
        ),
    )
}

private object AlbumsAnalyticsType {
    object SelectItem : Type("select_item")
}

private object AlbumsAnalyticsParamKeys {
    object ItemId : ParamKeys("item_id")
}
