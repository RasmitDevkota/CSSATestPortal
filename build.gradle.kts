import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
    id("org.jetbrains.compose") version "0.2.0-build132"
}

group = "alientech"
version = "1.0"

// Edit stuff later

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation( "com.squareup.okhttp3:okhttp:3.8.1")
    implementation("net.jemzart:jsonkraken:2.0.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "CSSA Test Portal"
            version = "0.2"
            description = "Test portal application for CSSA"
            copyright = "© 2020 CSSA. All rights reserved."
            vendor = "CSSA"

            windows {
                iconFile.set(project.file("src/main/resources/icon.ico"))
            }
        }
    }
}