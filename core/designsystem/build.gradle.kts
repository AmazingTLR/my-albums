plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.android.compose)
}

android {
    namespace = "fr.leboncoin.core.designsystem"

    // Shared Compose UI test helpers
    testFixtures {
        enable = true
    }
}

dependencies {
    // Spark is an implementation detail of the design system: features and core:ui only see the
    // App* wrappers exposed by this module.
    implementation(platform(libs.spark.bom))
    implementation(libs.spark)

    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesApi(libs.androidx.compose.ui.test)
    testFixturesApi(libs.androidx.compose.ui.test.junit4)
    testFixturesApi(libs.robolectric)
    testFixturesApi(libs.roborazzi)
    testFixturesApi(libs.roborazzi.compose)
    testFixturesApi(libs.roborazzi.junit)
    testFixturesImplementation(libs.androidx.test.core.ktx)
}
