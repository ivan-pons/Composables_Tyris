plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies{

    implementation(libs.kotlinx.serialization.json)
    //Paging
    implementation(libs.paging.common)
    implementation(libs.coroutines.core)
}