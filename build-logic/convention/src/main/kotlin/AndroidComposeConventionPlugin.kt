import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Adds Compose (compiler plugin, BOM, ui, previews) on top of an Android application or library. */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(pluginId("kotlin-compose"))

        pluginManager.withPlugin("com.android.application") {
            extensions.getByType(ApplicationExtension::class.java).buildFeatures.compose = true
        }
        pluginManager.withPlugin("com.android.library") {
            extensions.getByType(LibraryExtension::class.java).buildFeatures.compose = true
        }

        dependencies {
            val bom = platform(library("androidx-compose-bom"))
            add("implementation", bom)
            add("implementation", library("androidx-compose-ui"))
            add("implementation", library("androidx-compose-ui-graphics"))
            add("implementation", library("androidx-compose-ui-tooling-preview"))
            add("debugImplementation", library("androidx-compose-ui-tooling"))
        }
    }
}
