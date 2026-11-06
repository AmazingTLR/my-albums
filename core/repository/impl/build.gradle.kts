plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    api(project(":core:repository:api"))
    implementation(project(":core:common"))
    implementation(project(":core:network:api"))
}
