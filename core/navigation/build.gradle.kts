plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.android.compose)
}

android {
    namespace = "fr.leboncoin.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
}
