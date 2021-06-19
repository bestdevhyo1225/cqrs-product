import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

plugins {
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
}

allOpen {
    // MongoDB Document 키워드 Open
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}
