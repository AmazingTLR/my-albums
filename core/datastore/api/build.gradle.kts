plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(project(":core:model"))
    api(libs.kotlinx.coroutines.core)
}
