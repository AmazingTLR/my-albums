plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
