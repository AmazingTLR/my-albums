plugins {
    alias(libs.plugins.leboncoin.android.feature)
}

android {
    namespace = "fr.leboncoin.feature.details.impl"
}

dependencies {
    api(project(":feature:details:api"))
    implementation(project(":core:usecase"))
    implementation(project(":core:common"))
}
