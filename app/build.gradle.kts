plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
//    id("me.parade.dependency-exclude")
}

android {
    namespace = "me.parade.wanandroid"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "me.parade.wanandroid"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
        buildConfig = true
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {

    implementation(project(":lib_base"))
    implementation(project(":lib_image"))
    implementation(project(":lib_common"))

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.swiperefresh)

    implementation(libs.refresh.layout.kernel)
    implementation(libs.refresh.header.classics)
    implementation(libs.refresh.footer.classics)

    implementation(libs.retrofit){
        exclude(group = "com.squareup.okhttp3", module ="okhttp")
    }
    implementation(libs.angcyo.dsladapter){
        exclude(group = "androidx.recyclerview",module="recyclerview")
    }
    implementation(libs.angcyo.dslitem){
        exclude(group="com.github.angcyo:DslAdapter",module="DslAdapter")
    }
    implementation(libs.okhttp.client)
    implementation(libs.banner)
    implementation(libs.androidx.activity)


}