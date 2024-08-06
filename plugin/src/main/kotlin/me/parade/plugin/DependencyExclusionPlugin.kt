package me.parade.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.logging.Logger
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withModule

class DependencyExclusionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
//        project.configurations.all { configuration ->
//            configuration.resolutionStrategy.eachDependency {
//                if (requested.group == "androidx.appcompat" && requested.name == "appcompat") {
//                    useTarget("${requested},Exclude Activity")
//                    (target as? ModuleDependency)?.exclude(
//                        mapOf(
//                            "group" to "androidx.activity",
//                            "name" to "activity"
//                        )
//                    )
//                    because("Automatically excluding activity from com.example:appcompat")
//                } else if (requested.group == "com.google.android.material" && requested.name == "material") {
//                    (target as? ModuleDependency)?.exclude(
//                        mapOf(
//                            "group" to "androidx.activity",
//                            "name" to "activity"
//                        )
//                    )
//                }
//            }
//            true
//        }

        project.dependencies.components.withModule("androidx.appcompat:appcompat") {
            allVariants {
                withDependencies {
                    removeAll {
                        (it.group == "androidx.activity" && it.name == "activity")
                                || (it.group == "com.google.android.material" && it.name == "material")
                    }
                }
            }
        }
        project.dependencies.components.withModule("com.google.android.material:material") {
            allVariants {
                withDependencies {
                    removeAll {
                        it.group == "androidx.activity" && it.name == "activity"
                    }
                }
            }
        }


//        project.afterEvaluate {
//            project.configurations.forEach {configuration->
//                configuration.dependencies.forEach {dependency ->
//                    if (dependency is ModuleDependency){
//                        when{
//                            dependency.group == "androidx.appcompat" && dependency.name == "appcompat" -> {
//                                dependency.exclude(mapOf("group" to "androidx.activity","module" to "activity"))
//                                dependency.exclude(mapOf("group" to "com.google.android.material","module" to "material"))
//                                logger.info("Excluded androidx.activity:activity from androidx.appcompat:appcompat")
//                            }
//                            dependency.group == "com.google.android.material" && dependency.name == "material" -> {
//                                dependency.exclude(mapOf("group" to "androidx.activity","module" to "activity"))
//                                logger.info("Excluded androidx.activity:activity from com.google.android.material:material")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        project.subprojects {
//            configurations.all { configuration ->
//                configuration.resolutionStrategy.eachDependency {
//                    when{
//                        requested.group == "androidx.appcompat" && requested.name == "appcompat" -> {
//                            configuration.exclude(mapOf("group" to "androidx.activity","module" to "activity"))
//                            configuration.exclude(mapOf("group" to "com.google.android.material","module" to "material"))
//                        }
//                        requested.group == "com.google.android.material" && configuration.name == "material" -> {
//                            configuration.exclude(mapOf("group" to "androidx.activity","module" to "activity"))
//                        }
//                    }
//                }
//                true
//            }
//        }
    }


}