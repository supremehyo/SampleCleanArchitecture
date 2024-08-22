import com.hyoseok.samplecleanarchitecture.configureCoilAndroid
import com.hyoseok.samplecleanarchitecture.configureCoroutineAndroid
import com.hyoseok.samplecleanarchitecture.configureDataAndroid
import com.hyoseok.samplecleanarchitecture.configureHiltAndroid
import com.hyoseok.samplecleanarchitecture.configureKotest
import com.hyoseok.samplecleanarchitecture.configureKotlinAndroid
import com.hyoseok.samplecleanarchitecture.configurePagingAndroid

plugins {
    id("com.android.library")
    id("hyoseok.verify.detekt")
}

configureKotlinAndroid()
configureKotest()
configureCoroutineAndroid()
configureHiltAndroid()
