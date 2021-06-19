package com.hs.entity

import org.springframework.data.mongodb.repository.MongoRepository

interface ProductAggregateRepository : MongoRepository<ProductAggregate, String> {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate?
}
