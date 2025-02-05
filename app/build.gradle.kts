plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.0"


}

android {
    namespace = "com.example.fooddelivery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fooddelivery"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

    packaging {
        resources {
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE"
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.foundation:foundation:1.5.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation (libs.coil.compose)

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.33.0-alpha")
    implementation ("androidx.compose.material3:material3:1.1.1")
    implementation ("androidx.compose.runtime:runtime:1.4.3")

    implementation("org.osmdroid:osmdroid-android:6.1.14")
    implementation("org.osmdroid:osmdroid-wms:6.1.10")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.navigation:navigation-compose:2.6.0" )
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.firebase:firebase-messaging:23.1.1")
    implementation ("androidx.core:core-ktx:1.10.0")


    implementation ("com.google.accompanist:accompanist-pager:0.25.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.25.1")


    implementation (libs.androidx.foundation)
    implementation(libs.coil.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation ("androidx.navigation:navigation-compose:2.8.3")
    implementation ("androidx.compose.foundation:foundation:1.4.0")
    implementation ("androidx.compose.material3:material3:1.0.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.ktor.client.android)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.coroutines)
    implementation (libs.room.runtime)
    implementation (libs.room.ktx)

    /*implementation(libs.junit.junit)*/
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.accessibility.test.framework)
    implementation(libs.androidx.compose.material)
    implementation(libs.places)
    implementation(libs.engage.core)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.storage)
    implementation(libs.firebase.crashlytics.buildtools)
  //  testImplementation("junit:junit:4.13.2")
    implementation(libs.junit.junit)
   // kapt(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //navigation
    //implementation("androidx.navigation:navigation-compose:2.5.0")
    //icons
    implementation("androidx.compose.material:material-icons-extended")
    //image_supabase
    //implementation("io.coil-kt:coil-compose:2.3.0")




    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.3")

    implementation("androidx.credentials:credentials:1.3.0")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")

    // images
    implementation("io.coil-kt:coil:2.4.0")

    implementation ("com.google.code.gson:gson:2.8.8")

}