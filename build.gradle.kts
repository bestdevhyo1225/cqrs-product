import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.0" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false

    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30" apply false
    kotlin("plugin.jpa") version "1.4.30" apply false
    kotlin("plugin.allopen") version "1.4.30" apply false
    kotlin("plugin.noarg") version "1.4.30" apply false
    kotlin("plugin.serialization") version "1.4.30" apply false
    kotlin("kapt") version "1.4.30" apply false

    maven
}

repositories {
    mavenCentral()
    jcenter()
}

subprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-kapt")

    group = "com.hs.product"
    version = "1.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_11

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("au.com.console:kassava:2.1.0-rc.1")
        implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.amqp:spring-rabbit-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xallow-result-return-type")
            jvmTarget = "11"
        }
    }
}

project(":command") {
    dependencies {
        implementation(project(":common"))
        implementation(project(":domain:mysql"))
    }

    (tasks.getByName("processResources") as ProcessResources).apply {
        filesMatching("application.yml") {
            expand(project.properties)
        }
    }
}

project(":domain:mysql") {
    dependencies {
        implementation(project(":common"))
    }
}

project(":query") {
    dependencies {
        implementation(project(":common"))
    }

    (tasks.getByName("processResources") as ProcessResources).apply {
        filesMatching("application.yml") {
            expand(project.properties)
        }
    }
}
