plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
//    id("me.parade.dependency-exclude")
}

android {
    namespace = "me.parade.lib_base"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kotlin {
        jvmToolchain(11)
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

//    implementation(libs.androidx.navigation.fragment.ktx)//包含了Androidx.activity-ktx和Androidx.fragment-ktx
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fgragment.ktx)

    implementation(libs.retrofit){
        exclude(group = "com.squareup.okhttp3", module ="okhttp")
    }
    implementation(libs.okhttp.client)
    implementation(libs.retrofit.gson)
    implementation(libs.retrofit.scalars)

    implementation(libs.blankj.utilcodex)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
}