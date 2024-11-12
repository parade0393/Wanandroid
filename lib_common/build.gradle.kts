plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "me.parade.lib_common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    compileOnly(libs.androidx.recyclerview)
    compileOnly(libs.androidx.viewpager2)
    compileOnly(libs.retrofit.gson)
    compileOnly(libs.androidx.lifecycle.runtime)
    compileOnly(libs.androidx.appcompat)
    compileOnly(libs.androidx.swiperefresh)
    compileOnly(libs.androidx.coordinatorlayout)
    compileOnly(libs.google.material)
    compileOnly(libs.androidx.constraintlayout){
        exclude(group = "androidx.appcompat", module = "appcompat")
    }
}