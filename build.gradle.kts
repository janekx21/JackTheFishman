import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // Dokka javadoc/KDoc generation
    // see https://kotlinlang.org/docs/reference/kotlin-doc.html
    id("org.jetbrains.dokka") version "0.10.1"

    // Apply the application plugin to add support for building a CLI application.
    application
}

subprojects {
    // Apllying the root plungins for the subprojects
    // We need kotlin for the documentation generation
    apply(plugin = "java")
    apply(plugin = "kotlin")
}

allprojects {
    version = "1.0"

    repositories {
        // Use jcenter for resolving dependencies.
        // You can declare any Maven/Ivy/file repository here.
        jcenter()

        flatDir {
            // Adds lib dir as a repository
            dirs("lib")
        }
    }

    dependencies {
        // Align versions of all Kotlin components
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

        // Use the Kotlin JDK 8 standard library.
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        // Use the Kotlin test library.
        testImplementation("org.jetbrains.kotlin:kotlin-test")

        // Use the Kotlin JUnit integration.
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

        // Every lib in the lib folder needs do be includes
        implementation(fileTree("lib"))
        // TODO change dependencies to online repository
    }
}


application {
    // Define the main class for the application.
    // TODO define main class
}

tasks {
    // Generate documentation
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
        subProjects = listOf("DodgyDeliveries3")
    }
}