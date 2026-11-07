plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.android.room)
}

android {
    namespace = "fr.leboncoin.core.database.room"

    defaultConfig {
    }
}

dependencies {
    api(project(":core:database:api"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
