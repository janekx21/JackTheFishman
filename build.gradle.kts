import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask

version = "2.0"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.3.70"
    // Dokka javadoc/KDoc generation
    // see https://kotlinlang.org/docs/reference/kotlin-doc.html
    id("org.jetbrains.dokka") version "0.10.1"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://raw.github.com/SpinyOwl/repo/releases")
    }
}

dependencies {
    val koinVersion = "2.1.6"
    val lwjglVersion = "3.2.3"
    val jomlVersion = "1.9.25"

    // Kotlin standard library for runtime support
    implementation(kotlin("stdlib"))

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // Koin for dependency injection
    implementation("org.koin:koin-core:$koinVersion")
    testImplementation("org.koin:koin-test:$koinVersion")

    // Klaxon is a json serialisation library
    implementation("com.beust:klaxon:5.0.1")

    // Faster JSON parser (about twice on large json strings)
    implementation("com.beust:klaxon-jackson:1.0.1")

    // 2D Physics
    implementation("org.jbox2d:jbox2d-library:2.2.1.1")

    // legui
    implementation("org.liquidengine:legui:2.1.0")

    // LWJGL is a collection of game library's
    val os = getCurrentOperatingSystem()
    val lwjglNatives = when {
        os.isWindows -> "natives-windows"
        os.isMacOsX -> "natives-macos"
        os.isLinux -> "natives-linux"
        else -> throw IllegalArgumentException("OS not supported")
    }
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-tinyfd")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-tinyfd", classifier = lwjglNatives)
    api("org.joml", "joml", jomlVersion)

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.0.6")

    // Mocking Framework
    // testImplementation("io.mockk:mockk:{version}")
}

tasks {
    // Generate documentation with kotlin support
    @Suppress("UNUSED_VARIABLE")
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }
}
