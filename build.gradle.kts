plugins {
    `maven-publish`

    kotlin("jvm") version "2.0.0"
    id("net.kyori.blossom") version "2.1.0"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
}

// Library Properties
val buster_version: String by project

//ESL
val esl_version: String by project
val gson_version: String by project
val rx_streams_version: String by project
val reflections_version: String by project
val dateparser_version: String by project

val ktor_version: String by project

group = "com.busted_moments"
version = buster_version

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://jitpack.io")
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", buster_version)
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))

    //ESL
    implementation("com.google.code.gson:gson:$gson_version")
    implementation("org.reactivestreams:reactive-streams:$rx_streams_version")
    implementation("org.reflections:reflections:$reflections_version")
    implementation("com.github.sisyphsu:dateparser:$dateparser_version")

    implementation("com.github.essentuan:esl:v$esl_version")

    implementation("io.ktor:ktor-websockets:$ktor_version")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "buster"

            from(components["java"])
        }
    }
}