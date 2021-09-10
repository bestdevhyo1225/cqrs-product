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

        return ProductAggregate.toDomainEntity(
            id = productAggregateDocument.id,
            productId = productAggregateDocument.productId,
            type = productAggregateDocument.type,
            isDisplay = productAggregateDocument.isDisplay,
            data = productAggregateDocument.data,
            createdDatetime = productAggregateDocument.createdDatetime,
            updatedDatetime = productAggregateDocument.updatedDatetime
        )
    }

    override fun insertAll(productAggregates: List<ProductAggregate>) {
        val bulkOperations: BulkOperations =
            mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, "product_aggregates")

        val productAggregateDocuments: List<ProductAggregateDocument> = productAggregates.map {
            ProductAggregateDocument.toPersistenceEntity(
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
                ProductAggregateDocument.toPersistenceEntity(
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
