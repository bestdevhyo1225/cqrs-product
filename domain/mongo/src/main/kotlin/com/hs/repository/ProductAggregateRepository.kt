package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductAggregateRepository : MongoRepository<ProductAggregate, String> {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate?

    fun findByProductIdAndTypeAndIsDisplay(
        productId: Long,
        type: ProductAggregateType,
        isDisplay: String = "true"
    ): ProductAggregate?
}
