// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply  false//com.android.application 8.1.4
    alias(libs.plugins.kotlin.android) apply false//org.jetbrains.kotlin.android 2.0.0
    alias(libs.plugins.android.library) apply false//com.android.library 8.1.4
    alias(libs.plugins.kotlin.kapt) apply false
}