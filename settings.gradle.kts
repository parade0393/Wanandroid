pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
//        maven {
//            url = uri("./local_maven")
//        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Wanandroid"
include(":app")
include(":lib_base")
include(":plugin")
include(":lib_image")
include(":lib_demo")
include(":lib_okhttp_log")
include(":lib_common")
include(":lib_dslitem")
