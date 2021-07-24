package com.hs.infrastructure.mongo

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class ProductAggregateRepositoryImpl(private val mongoOperations: MongoOperations) :
    QueryAppProductAggregateRepository {

    override fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate? {
        val query = Query(
            Criteria
                .where("productId").isEqualTo(productId)
                .and("type").isEqualTo(type)
        )

        return mongoOperations.findOne(query, ProductAggregate::class.java)
    }

    override fun findByProductIdAndTypeAndIsDisplay(
        productId: Long,
        type: ProductAggregateType,
        isDisplay: Boolean
    ): ProductAggregate? {
        val query = Query(
            Criteria
                .where("productId").isEqualTo(productId)
                .and("type").isEqualTo(type)
                .and("isDisplay").isEqualTo(isDisplay)
        )

        return mongoOperations.findOne(query, ProductAggregate::class.java)
    }

    override fun insert(productAggregate: ProductAggregate): ProductAggregate? {
        return mongoOperations.insert(productAggregate)
    }

    override fun save(productAggregate: ProductAggregate): ProductAggregate? {
        return mongoOperations.save(productAggregate)
    }
}
