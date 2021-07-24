package com.hs.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

@Configuration
class MongoDBConfig(
    @Value("\${spring.data.mongodb.uri}")
    private val mongodbUri: String
) {

    @Bean
    fun mongoTemplate(): MongoTemplate {
        val factory: MongoDatabaseFactory = SimpleMongoClientDatabaseFactory(mongodbUri)

        val converter = MappingMongoConverter(DefaultDbRefResolver(factory), MongoMappingContext())

        converter.setTypeMapper(DefaultMongoTypeMapper(null))

        return MongoTemplate(factory, converter)
    }
}
