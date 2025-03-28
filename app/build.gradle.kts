plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.chatboot"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chatboot"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"
           multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    viewBinding {
        viewBinding.isEnabled = true // This enables ViewBinding

    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation ("com.google.firebase:firebase-messaging:24.1.0")
    implementation ("com.google.firebase:firebase-firestore:25.1.1")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    implementation("com.makeramen:roundedimageview:2.3.0")

    implementation("androidx.multidex:multidex:2.0.1")
}