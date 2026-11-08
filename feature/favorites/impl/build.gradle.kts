plugins {
    alias(libs.plugins.leboncoin.android.feature)
}

android {
    namespace = "fr.leboncoin.feature.favorites.impl"
}

dependencies {
    api(project(":feature:favorites:api"))
    implementation(project(":core:usecase"))
    implementation(project(":core:common"))
}
