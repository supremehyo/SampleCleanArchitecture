plugins {
    id("hyoseok.android.feature")
}

android {
    namespace = "feature.main"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.immutable)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.serialization.json)

    implementation(project(mapOf("path" to ":feature:gallery")))
    implementation(project(mapOf("path" to ":feature:camera")))
    implementation(project(mapOf("path" to ":feature:home")))
}