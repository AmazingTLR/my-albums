import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention for `:feature:<name>:impl` modules: Android library + Compose + Koin + ViewModel,
 * plus the shared building blocks every feature relies on.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("leboncoin.android.library")
        pluginManager.apply("leboncoin.android.compose")

        dependencies {
            add("api", project(":core:navigation"))
            add("api", project(":core:designsystem"))
            add("api", project(":core:model"))
            add("testImplementation", project(":core:testing"))
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:analytics:api"))

            add("implementation", library("kotlinx-coroutines-core"))
            add("implementation", platform(library("koin-bom")))
            add("api", library("koin-core"))
            add("implementation", library("koin-android"))
            add("implementation", library("koin-androidx-compose"))

            add("implementation", library("androidx-navigation3-runtime"))
            add("implementation", library("androidx-lifecycle-viewmodel-compose"))
            add("implementation", library("androidx-lifecycle-runtimeCompose"))
        }
    }
}
