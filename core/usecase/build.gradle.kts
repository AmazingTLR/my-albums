plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(project(":core:model"))
    api(project(":core:repository:api"))
    implementation(project(":core:common"))
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}
