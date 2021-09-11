plugins {
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain:mysql"))
    implementation(project(":infrastructure:rabbitmq"))
    implementation(project(":infrastructure:rdbms"))

    implementation("org.springframework:spring-tx:5.2.16.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
