import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(pluginId("ksp"))

        extensions.configure<KspExtension> {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        dependencies {
            add("implementation", library("androidx-room-runtime"))
            add("implementation", library("androidx-room-ktx"))
            add("ksp", library("androidx-room-compiler"))
        }
    }
}
