plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "eternal.future.tefmodloader"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "eternal.future.tefmodloader"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 100306
        versionName = "0.10.3.6"
        multiDexEnabled = true
    }

    splits.abi {
        isEnable = true
        reset()
        include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            pickFirsts.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
        }
        jniLibs {
            excludes.addAll(
                listOf(
                    "**/libTEFModLoader.so",
                    "**/libauxiliary.so",
                    "**/libTEFLoader.so",
                    "**/libdobby.so",
                    "**/libexample1.so",
                    "**/libexample2.so"
                )
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions.jvmTarget = "17"
    buildFeatures.compose = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.android)
    implementation(libs.material.icons.extended)
    implementation(libs.tomlkt)
    implementation(libs.apkzlib)
    implementation(project(":android:core"))
    implementation(project(":android:axml"))
}