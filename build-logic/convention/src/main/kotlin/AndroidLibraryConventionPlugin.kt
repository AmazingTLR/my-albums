import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(pluginId("android-library"))
        extensions.configure<LibraryExtension> { configureLibrary(this) }
        configureUnitTestDependencies()
        // Robolectric 4.17 reaches into JDK internals to fake file descriptors.
        tasks.withType<Test>().configureEach {
            jvmArgs("--add-exports=java.base/jdk.internal.access=ALL-UNNAMED")
        }
    }
}
