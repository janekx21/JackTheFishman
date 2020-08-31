val applicationMainClassName = "dodgyDeliveries3.DodgyDeliveries3Kt"

plugins {
    application
    distribution
    kotlin("jvm")
}

application {
    // Define the main class for the application
    mainClassName = applicationMainClassName
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = applicationMainClassName
    }

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
