plugins {
    id("hyoseok.android.library")
    id("hyoseok.android.compose")
}

android {
    namespace = "com.hyoseok.samplecleanarchitecture.core.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}