package com.hs.config.mongo

import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import javax.annotation.PostConstruct

@Configuration
class MongoConfig(
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

    @PostConstruct
    fun ensureIndexes() {
        val mongoOperations: MongoOperations = mongoTemplate()

        // 단일 조회를 위한 Index
        mongoOperations
            .indexOps("product_aggregates")
            .ensureIndex(
                CompoundIndexDefinition(
                    Document()
                        .append("productId", 1)
                        .append("isDisplay", 1)
                )
            )

        // 리스트 조회를 위한 Index
        mongoOperations
            .indexOps("product_aggregates")
            .ensureIndex(
                CompoundIndexDefinition(
                    Document()
                        .append("isDisplay", 1)
                        .append("productId", -1)
                        .append("createdDatetime", -1)
                )
            )
    }
}
