plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "fr.leboncoin.feature.albums.api"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
}
