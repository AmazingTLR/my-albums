plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "fr.leboncoin.feature.details.api"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(project(":core:navigation"))
}
