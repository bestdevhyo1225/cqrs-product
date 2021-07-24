package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class ProductAggregateMongoOperations(private val mongoOperations: MongoOperations) :
    BatchAppProductAggregateRepository {

    override fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate? {
        val query = Query(Criteria.where("productId").isEqualTo(productId).and("type").isEqualTo(type))

        return mongoOperations.findOne(query, ProductAggregate::class.java)
    }

    override fun insertAll(productAggregates: List<ProductAggregate>) {
        val bulkOperations: BulkOperations =
            mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, "product_aggregates")

        bulkOperations.insert(productAggregates)
        bulkOperations.execute()
    }

    override fun saveAll(productAggregates: List<ProductAggregate>) {
        productAggregates.forEach { productAggregate -> mongoOperations.save(productAggregate) }
    }
}
