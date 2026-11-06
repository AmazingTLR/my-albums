plugins {
    alias(libs.plugins.leboncoin.android.library)
}

android {
    namespace = "fr.leboncoin.core.analytics.impl"
}

dependencies {
    api(project(":core:analytics:api"))
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}
