import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.6"
}

group = "de.thm.asc.tiel"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("TiEL")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "de.thm.asc.tiel.interpreter.TiEL"))
        }
        archiveClassifier = null
    }
    build {
        dependsOn(shadowJar)
    }
}

tasks.test {
    useJUnitPlatform()
    include("**/de/thm/asc/tiel/interpreter/**")
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
    outputs.upToDateWhen { false }
}