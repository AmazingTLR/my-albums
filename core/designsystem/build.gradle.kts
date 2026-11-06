plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.android.compose)
}

android {
    namespace = "fr.leboncoin.core.designsystem"
}

dependencies {
    // Spark is an implementation detail of the design system: features and core:ui only see the
    // App* wrappers exposed by this module.
    implementation(platform(libs.spark.bom))
    implementation(libs.spark)

}
