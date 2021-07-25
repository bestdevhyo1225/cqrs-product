plugins {
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("com.querydsl:querydsl-jpa:4.4.0")
    runtimeOnly("mysql:mysql-connector-java")
    testImplementation("org.springframework.batch:spring-batch-test")
}
