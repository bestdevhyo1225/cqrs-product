plugins {
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
}

allOpen {
    // MongoDB Document 키워드 Open
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    implementation(project(":domain:mongo"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}
