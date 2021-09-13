package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateDocument
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class ProductAggregateRepositoryImpl(private val mongoOperations: MongoOperations) :
    BatchAppProductAggregateRepository {

    override fun findByProductId(productId: Long): ProductAggregate? {
        val query = Query(Criteria.where("productId").isEqualTo(productId))

        val productAggregateDocument: ProductAggregateDocument =
            mongoOperations.findOne(query, ProductAggregateDocument::class.java) ?: return null

        return ProductAggregate.mapOf(
            id = productAggregateDocument.id!!,
            productId = productAggregateDocument.productId,
            productInfo = productAggregateDocument.productInfo,
            isDisplay = productAggregateDocument.isDisplay,
            createdDatetime = productAggregateDocument.createdDatetime,
            updatedDatetime = productAggregateDocument.updatedDatetime
        )
    }

    override fun insertAll(productAggregates: List<ProductAggregate>) {
        val bulkOperations: BulkOperations =
            mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, "product_aggregates")

        val productAggregateDocuments: List<ProductAggregateDocument> = productAggregates.map {
            ProductAggregateDocument.create(
                productId = it.productAggregateId.getProductId(),
                isDisplay = it.isDisplay,
                productInfo = it.productInfo,
                createdDatetime = it.getStringCreatedDatetime(),
                updatedDatetime = it.getStringUpdatedDatetime()
            )
        }

        bulkOperations.insert(productAggregateDocuments)
        bulkOperations.execute()
    }

    override fun saveAll(productAggregates: List<ProductAggregate>) {
        productAggregates.forEach {
            mongoOperations.save(
                ProductAggregateDocument.create(
                    id = it.productAggregateId.getId(),
                    productId = it.productAggregateId.getProductId(),
                    isDisplay = it.isDisplay,
                    productInfo = it.productInfo,
                    createdDatetime = it.getStringCreatedDatetime(),
                    updatedDatetime = it.getStringUpdatedDatetime()
                )
            )
        }
    }
}
