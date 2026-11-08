plugins {
    alias(libs.plugins.leboncoin.android.library)
}

android {
    namespace = "fr.leboncoin.core.datastore.preferences"
}

dependencies {
    api(project(":core:datastore:api"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
}
