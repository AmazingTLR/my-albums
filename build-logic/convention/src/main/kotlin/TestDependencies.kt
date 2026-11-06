import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Unit-test baseline applied to every module: JUnit4, mockk, coroutines-test and Turbine. */
internal fun Project.configureUnitTestDependencies() {
    dependencies {
        add("testImplementation", library("junit"))
        add("testImplementation", library("mockk"))
        add("testImplementation", library("kotlinx-coroutines-test"))
        add("testImplementation", library("turbine"))
    }
}
