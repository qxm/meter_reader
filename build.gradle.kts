plugins {
    kotlin("jvm") version "2.3.10"
    application
}

application {
    // Replace with your fully qualified main class name
    mainClass.set("org.example.MainKt")
}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
