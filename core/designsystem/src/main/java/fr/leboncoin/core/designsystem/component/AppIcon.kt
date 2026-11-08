package fr.leboncoin.core.designsystem.component

import com.adevinta.spark.icons.HeartFill
import com.adevinta.spark.icons.HeartOutline
import com.adevinta.spark.icons.ImageFill
import com.adevinta.spark.icons.ImageOutline
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.SparkIcon

/** An icon of the design system. Wraps the Spark icon so other modules never depend on Spark. */
class AppIcon internal constructor(internal val spark: SparkIcon)

object AppIcons {
    val HeartFilled = AppIcon(LeboncoinIcons.HeartFill)
    val HeartOutlined = AppIcon(LeboncoinIcons.HeartOutline)
    val ImageFilled = AppIcon(LeboncoinIcons.ImageFill)
    val ImageOutlined = AppIcon(LeboncoinIcons.ImageOutline)
}
