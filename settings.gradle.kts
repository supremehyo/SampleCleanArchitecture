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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SampleCleanArchitecture"
include(":app")
include(":feature:main")
include(":core:model")
include(":core:usecase")
include(":core:repository")
include(":core:designsystem")
include(":data:api")
include(":data:repository")
include(":data:model")
include(":data:remote")
include(":compose_camera")
include(":feature:camera")
