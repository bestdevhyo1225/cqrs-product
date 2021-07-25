package com.hs.config

import org.bson.Document
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition
import javax.annotation.PostConstruct

@Configuration
class MongoIndexConfig(private val mongoOperations: MongoOperations) {

    @PostConstruct
    fun ensureIndexes() {
        // 단일 조회를 위한 Index
        mongoOperations
            .indexOps("product_aggregates")
            .ensureIndex(
                CompoundIndexDefinition(
                    Document()
                        .append("productId", 1)
                        .append("type", 1)
                        .append("isDisplay", 1)
                )
            )

        // 리스트 조회를 위한 Index
        mongoOperations
            .indexOps("product_aggregates")
            .ensureIndex(
                CompoundIndexDefinition(
                    Document()
                        .append("type", 1)
                        .append("isDisplay", 1)
                        .append("createdDatetime", -1)
                )
            )
    }
}
