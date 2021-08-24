package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateDocument
import com.hs.entity.ProductAggregateType
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ProductAggregateRepositoryImpl(private val mongoOperations: MongoOperations) :
    BatchAppProductAggregateRepository {

    override fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate? {
        val query = Query(Criteria.where("productId").isEqualTo(productId).and("type").isEqualTo(type))

        val productAggregateDocument: ProductAggregateDocument? =
            mongoOperations.findOne(query, ProductAggregateDocument::class.java)

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

    override fun insertAll(productAggregates: List<ProductAggregate>) {
        val bulkOperations: BulkOperations =
            mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, "product_aggregates")

        val productAggregateDocuments: List<ProductAggregateDocument> = productAggregates.map {
            ProductAggregateDocument(
                productId = it.productId,
                type = it.type,
                isDisplay = it.isDisplay,
                data = it.data,
                createdDatetime = it.createdDatetime,
                updatedDatetime = it.updatedDatetime
            )
        }

        bulkOperations.insert(productAggregateDocuments)
        bulkOperations.execute()
    }

    override fun saveAll(productAggregates: List<ProductAggregate>) {
        productAggregates.forEach {
            mongoOperations.save(
                ProductAggregateDocument(
                    id = it.id,
                    productId = it.productId,
                    type = it.type,
                    isDisplay = it.isDisplay,
                    data = it.data,
                    createdDatetime = it.createdDatetime,
                    updatedDatetime = it.updatedDatetime
                )
            )
        }
    }
}
