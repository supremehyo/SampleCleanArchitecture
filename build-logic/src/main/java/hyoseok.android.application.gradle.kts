import com.hyoseok.samplecleanarchitecture.configureCoilAndroid
import com.hyoseok.samplecleanarchitecture.configureHiltAndroid
import com.hyoseok.samplecleanarchitecture.configureKotestAndroid
import com.hyoseok.samplecleanarchitecture.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureCoilAndroid()


dependencies {
    /*
    implementation(project(":core:model"))
    implementation(project(":core:data"))

     */
}
