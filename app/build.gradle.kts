import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

}

android {
    namespace = "com.example.myslog"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myslog"
        minSdk = 27
        targetSdk = 36
        versionCode = 1051
        versionName = "1.0.51"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    // Cargar propiedades del keystore
    val propsFile = rootProject.file("signing/signing.properties")
    val keyFile = rootProject.file("signing/myslog.jks")
    val props = Properties().apply { load(FileInputStream(propsFile)) }
    signingConfigs {
        create("release") {
            storeFile = keyFile
            storePassword = props["storePassword"] as String
            keyAlias = props["keyAlias"] as String
            keyPassword = props["keyPassword"] as String
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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

    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)

        }
    }

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)

        //Dagger - Hilt
        implementation(libs.hilt.android)
        ksp(libs.hilt.android.compiler)
        implementation(libs.androidx.hilt.navigation.compose)
        ksp(libs.androidx.hilt.compiler)

        // Room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        ksp(libs.androidx.room.compiler)
        //Timber
        implementation(libs.timber)

        // Material 3
        implementation(libs.material3)
        //gson
        implementation(libs.gson)

        //splashScreen
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.androidx.material.icons.extended)

        // Coil
        implementation(libs.coil.compose)

        //markdown
        implementation(libs.compose.markdown)
        implementation(libs.sqlite.ktx)

    }
}
dependencies {
    implementation(libs.androidx.datastore.core)
}
