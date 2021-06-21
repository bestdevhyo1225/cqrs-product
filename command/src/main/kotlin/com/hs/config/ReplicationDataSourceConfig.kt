package com.hs.config

import com.hs.config.property.ReadProperty
import com.hs.config.property.WriteProperty
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
@EnableConfigurationProperties(value = [WriteProperty::class, ReadProperty::class])
class ReplicationDataSourceConfig(
    private val writeProperty: WriteProperty,
    private val readProperty: ReadProperty
) {

    @Bean
    fun writeDataSource(): DataSource {
        val dataSource = DataSourceBuilder.create()
            .driverClassName(writeProperty.driverClassName)
            .url(writeProperty.jdbcUrl)
            .username(writeProperty.username)
            .password(writeProperty.password)
            .type(HikariDataSource::class.java)
            .build()

        dataSource.poolName = "HikariDataSource [ Write ]"
        dataSource.minimumIdle = writeProperty.minimumIdle
        dataSource.maximumPoolSize = writeProperty.maximumPoolSize
        dataSource.maxLifetime = writeProperty.maxLifetime

        return dataSource
    }

    @Bean
    fun readDataSource(): DataSource {
        val dataSource = DataSourceBuilder.create()
            .driverClassName(readProperty.driverClassName)
            .url(readProperty.jdbcUrl)
            .username(readProperty.username)
            .password(readProperty.password)
            .type(HikariDataSource::class.java)
            .build()

        dataSource.poolName = "HikariDataSource [ Read ]"
        dataSource.minimumIdle = readProperty.minimumIdle
        dataSource.maximumPoolSize = readProperty.maximumPoolSize
        dataSource.maxLifetime = readProperty.maxLifetime

        return dataSource
    }

    @Bean
    fun routingDataSource(): DataSource {
        val dataSourceMap: MutableMap<Any, Any> = HashMap()

        val writeDataSource = writeDataSource()
        val readDataSource = readDataSource()

        dataSourceMap[ReplicationDataSourceType.WRITE] = writeDataSource
        dataSourceMap[ReplicationDataSourceType.READ] = readDataSource

        val replicationRoutingDataSource = ReplicationRoutingDataSource()
        replicationRoutingDataSource.setTargetDataSources(dataSourceMap)
        replicationRoutingDataSource.setDefaultTargetDataSource(writeDataSource)

        return replicationRoutingDataSource
    }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource())
    }
}
