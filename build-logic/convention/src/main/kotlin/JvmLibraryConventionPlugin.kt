import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

/** Convention for pure Kotlin/JVM modules (no Android SDK). */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("java-library")
        pluginManager.apply(pluginId("jetbrains-kotlin-jvm"))
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = ProjectJavaVersion
            targetCompatibility = ProjectJavaVersion
        }
        configureKotlinJvmTarget()
        configureUnitTestDependencies()
    }
}
