package com.hs.entity

import org.springframework.data.mongodb.repository.MongoRepository

interface ProductAggreagateRepository : MongoRepository<ProductAggreagte, String> {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggreagte?
}
