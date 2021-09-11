import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

plugins {
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
