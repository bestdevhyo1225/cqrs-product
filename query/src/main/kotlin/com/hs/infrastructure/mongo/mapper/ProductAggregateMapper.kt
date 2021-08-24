package com.hs.infrastructure.mongo.mapper

import com.hs.entity.ProductAggregate
import com.hs.infrastructure.mongo.persistence.ProductAggregateDocument
import java.time.LocalDateTime

class ProductAggregateMapper {

    companion object {
        fun toDocument(productAggregate: ProductAggregate): ProductAggregateDocument {
            return ProductAggregateDocument(
                id = productAggregate.id,
                productId = productAggregate.productId,
                type = productAggregate.type,
                isDisplay = productAggregate.isDisplay,
                data = productAggregate.data,
                createdDatetime = productAggregate.createdDatetime,
                updatedDatetime = productAggregate.updatedDatetime
            )
        }

        fun toDomainEntity(productAggregateDocument: ProductAggregateDocument?): ProductAggregate? {
            productAggregateDocument ?: return null

            val createdDatetime =
                LocalDateTime.parse(productAggregateDocument.createdDatetime, ProductAggregate.DATETIME_FORMATTER)

            val updatedDatetime =
                LocalDateTime.parse(productAggregateDocument.updatedDatetime, ProductAggregate.DATETIME_FORMATTER)

            return ProductAggregate(
                id = productAggregateDocument.id,
                productId = productAggregateDocument.productId,
                type = productAggregateDocument.type,
                isDisplay = productAggregateDocument.isDisplay,
                data = productAggregateDocument.data,
                createdDatetime = createdDatetime,
                updatedDatetime = updatedDatetime
            )
        }
    }
}
