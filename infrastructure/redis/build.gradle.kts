plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
