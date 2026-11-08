import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Adds Compose (compiler plugin, BOM, ui, previews) on top of an Android application or library.
 * Android libraries also get the Compose UI test setup (Robolectric + Roborazzi), see
 * `UI_TESTING_GUIDELINES.md`.
 */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(pluginId("kotlin-compose"))

        pluginManager.withPlugin("com.android.application") {
            extensions.getByType(ApplicationExtension::class.java).buildFeatures.compose = true
        }
        pluginManager.withPlugin("com.android.library") {
            extensions.getByType(LibraryExtension::class.java).buildFeatures.compose = true
            configureComposeUiTests()
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

private const val DESIGN_SYSTEM_PATH = ":core:designsystem"

/** Robolectric + Roborazzi setup shared by every Android library that has Compose UI tests. */
private fun Project.configureComposeUiTests() {
    pluginManager.apply(pluginId("roborazzi"))

    extensions.configure<LibraryExtension> {
        testOptions {
            // Lets tests resolve string resources and themes.
            unitTests.isIncludeAndroidResources = true
        }
        sourceSets {
            // Screenshot tests live in `src/screenshotTest` but run as plain unit tests.
            getByName("test").kotlin.directories.add("src/screenshotTest/java")
        }
    }

    // Goldens are committed next to the sources so they appear in pull request diffs.
    extensions.configure<RoborazziExtension> {
        outputDir.set(layout.projectDirectory.dir("src/test/screenshots"))
    }

    dependencies {
        add("testImplementation", library("robolectric"))
        add("testImplementation", library("roborazzi"))
        add("testImplementation", library("roborazzi-compose"))
        add("testImplementation", library("roborazzi-junit"))
        add("testImplementation", library("androidx-compose-ui-test"))
        add("testImplementation", library("androidx-compose-ui-test-junit4"))
        add("testImplementation", testFixtures(project(DESIGN_SYSTEM_PATH)))
        // Provides the ComponentActivity that createComposeRule() launches.
        add("debugImplementation", library("androidx-compose-ui-test-manifest"))
    }
}
