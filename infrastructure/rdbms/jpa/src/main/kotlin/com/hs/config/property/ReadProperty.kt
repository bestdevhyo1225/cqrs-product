package com.hs.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(value = "spring.datasource.hikari.read")
data class ReadProperty(
    val driverClassName: String,
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val minimumIdle: Int,
    val maximumPoolSize: Int,
    val maxLifetime: Long,
)
