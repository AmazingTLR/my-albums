plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.android.compose)
}

android {
    namespace = "fr.leboncoin.core.ui"
}

dependencies {
    api(project(":core:designsystem"))
    api(project(":core:model"))
    api(project(":core:analytics:api"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
