plugins {
    id("hyoseok.android.feature")
}
android {
    namespace = "com.hyoseok.compose_camera"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    // CameraX core library using camera2 implementation
    implementation("androidx.camera:camera-camera2:1.4.0-beta02")
    // CameraX Lifecycle Library
    implementation("androidx.camera:camera-lifecycle:1.4.0-beta02")
    // CameraX View class
    implementation ("androidx.camera:camera-view:1.4.0-beta02")
}