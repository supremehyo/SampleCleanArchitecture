import com.hyoseok.samplecleanarchitecture.configureKotest
import com.hyoseok.samplecleanarchitecture.configureKotlin

plugins {
    kotlin("jvm")
    id("hyoseok.verify.detekt")
}

configureKotlin()
configureKotest()
