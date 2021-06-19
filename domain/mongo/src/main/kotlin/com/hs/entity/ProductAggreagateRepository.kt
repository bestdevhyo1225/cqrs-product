package com.hs.entity

import org.springframework.data.mongodb.repository.MongoRepository

interface ProductAggreagateMopnRepository : MongoRepository<ProductAggreagte, String> {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggreagte?
}
