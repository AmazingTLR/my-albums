plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(platform(libs.koin.bom))
    api(libs.koin.core)
}
