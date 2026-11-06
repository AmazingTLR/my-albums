plugins {
    alias(libs.plugins.leboncoin.android.feature)
}

android {
    namespace = "fr.leboncoin.feature.albums.impl"
}

dependencies {
    api(project(":feature:albums:api"))
    implementation(project(":feature:details:api"))
    implementation(project(":core:usecase"))
    implementation(project(":core:common"))
}
