pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Android RecruitmentTest App"

include(":app")

include(":core:model")
include(":core:common")
include(":core:network:api")
include(":core:network:retrofit")
include(":core:repository:api")
include(":core:repository:impl")
include(":core:usecase")
include(":core:analytics:api")
include(":core:analytics:impl")
include(":core:navigation")
include(":core:designsystem")
include(":core:ui")

include(":feature:albums:api")
include(":feature:albums:impl")
include(":feature:details:api")
include(":feature:details:impl")
include(":core:testing")
