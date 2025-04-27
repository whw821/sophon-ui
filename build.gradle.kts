import org.gradle.kotlin.dsl.testImplementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ksp)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation(libs.datastore)
    implementation(libs.protobuf)
    implementation(compose.material3)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)
    implementation(libs.voyager.bottom.sheet.nav)
    implementation(libs.voyager.tab.nav)
    implementation(libs.voyager.transitions)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0-RC")

    implementation(project(":processor"))
    testImplementation(libs.junit.jupiter)
    ksp(project(":processor"))
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SophonUI"
            packageVersion = "1.0.0"
            includeAllModules = true

            macOS {
                // 设置图标
                iconFile.set(project.file("launcher/icon.icns"))
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
