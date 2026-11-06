plugins {
    alias(libs.plugins.leboncoin.android.application)
    alias(libs.plugins.leboncoin.android.compose)
}

android {
    namespace = "fr.leboncoin.androidrecruitmenttestapp"

    defaultConfig {
        applicationId = "fr.leboncoin.androidrecruitmenttestapp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Wiring: the app is the only module that depends on the *:impl / network implementation modules.
    implementation(project(":core:common"))
    implementation(project(":core:analytics:api"))
    implementation(project(":core:analytics:impl"))
    implementation(project(":core:network:retrofit"))
    implementation(project(":core:repository:impl"))
    implementation(project(":core:usecase"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":feature:albums:api"))
    implementation(project(":feature:albums:impl"))
    implementation(project(":feature:details:api"))
    implementation(project(":feature:details:impl"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.ui)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    debugImplementation(libs.leakcanary.android)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
