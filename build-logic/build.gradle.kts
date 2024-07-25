plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.verify.detektPlugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "com.hyoseok.hilt"
            implementationClass = "com.hyoseok.samplecleanarchitecture.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "example.kotlin.hilt"
            implementationClass = "com.hyoseok.samplecleanarchitecture.HiltKotlinPlugin"
        }
    }
}
