val accompanistVersion: String by project
val composeVersion: String by project
val lifecycleVersion: String by project

plugins {
    id("com.android.application") version "7.1.3"
    kotlin("android") version "1.6.10"
}

android {

    compileSdk = 32
    namespace = "dev.thielker.playground.android"

    defaultConfig {

        applicationId = "dev.thielker.playground.android"

        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

        debug {  }

        release { isMinifyEnabled = true }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    packagingOptions {

        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // android core
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.activity:activity-compose:1.4.0")

    // android lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")

    // jetpack compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.4.2")

    // material design
    implementation("androidx.compose.material3:material3:1.0.0-alpha09")
    implementation("com.google.android.material:material:1.5.0")

    // timber logging
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("androidx.compose.material:material:1.1.1")

    // androidx testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // jetpack compose testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}
