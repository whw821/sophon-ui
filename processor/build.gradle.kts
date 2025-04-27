@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("jvm")
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.16.0")
    implementation(libs.ksp)
}