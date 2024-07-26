import com.hyoseok.samplecleanarchitecture.configureHiltAndroid
import com.hyoseok.samplecleanarchitecture.configurePagingAndroid
import com.hyoseok.samplecleanarchitecture.libs

plugins {
    id("hyoseok.android.library")
    id("hyoseok.android.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()
configurePagingAndroid()

dependencies {
    implementation(project(":core:repository"))
    implementation(project(":core:usecase"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))

    val libs = project.extensions.libs
    implementation(libs.findLibrary("hilt.navigation.compose").get())
    implementation(libs.findLibrary("androidx.compose.navigation").get())
    androidTestImplementation(libs.findLibrary("androidx.compose.navigation.test").get())

    implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
    implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
    implementation(libs.findLibrary("compose.coil").get())
    implementation(libs.findLibrary("compose.paging").get())
}
