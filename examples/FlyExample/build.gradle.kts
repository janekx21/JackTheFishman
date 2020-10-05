plugins {
    application
    kotlin("jvm") version "1.3.70"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://raw.github.com/SpinyOwl/repo/releases")
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation(":JackTheFishman")
}

application {
    mainClass.set("FlyExampleKt")
}


