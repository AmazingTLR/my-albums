plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(project(":core:network:api"))
    implementation(project(":core:common"))

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    testImplementation(libs.okhttp.mockwebserver)
}
