import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/** Project-wide SDK levels, shared by every Android module. */
internal object AndroidConfig {
    const val COMPILE_SDK = 37
    const val MIN_SDK = 24
    const val TARGET_SDK = 36
}

internal val ProjectJavaVersion = JavaVersion.VERSION_11
internal val ProjectJvmTarget = JvmTarget.JVM_11

internal fun Project.configureApplication(extension: ApplicationExtension) {
    extension.apply {
        compileSdk { version = release(AndroidConfig.COMPILE_SDK) }
        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK
            targetSdk = AndroidConfig.TARGET_SDK
        }
        compileOptions {
            sourceCompatibility = ProjectJavaVersion
            targetCompatibility = ProjectJavaVersion
        }
    }
    configureKotlinJvmTarget()
}

internal fun Project.configureLibrary(extension: LibraryExtension) {
    extension.apply {
        compileSdk { version = release(AndroidConfig.COMPILE_SDK) }
        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK
        }
        compileOptions {
            sourceCompatibility = ProjectJavaVersion
            targetCompatibility = ProjectJavaVersion
        }
    }
    configureKotlinJvmTarget()
}

internal fun Project.configureKotlinJvmTarget() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(ProjectJvmTarget)
    }
}
