plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(project(":core:model"))
}
