plugins {
    id("hyoseok.android.application")
    id("hyoseok.android.compose")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.hyoseok.samplecleanarchitecture"

    defaultConfig {
        applicationId = "com.hyoseok.samplecleanarchitecture"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.immutable)

    implementation(project(mapOf("path" to ":feature:main")))

}
