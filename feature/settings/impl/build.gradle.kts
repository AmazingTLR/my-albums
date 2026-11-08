plugins {
    alias(libs.plugins.leboncoin.android.feature)
}

android {
    namespace = "fr.leboncoin.feature.settings.impl"
}

dependencies {
    api(project(":feature:settings:api"))
    implementation(project(":core:usecase"))
    implementation(project(":core:common"))
}
