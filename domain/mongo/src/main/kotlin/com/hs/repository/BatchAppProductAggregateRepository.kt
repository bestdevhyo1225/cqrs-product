package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType

interface BatchAppProductAggregateRepository {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate?
    fun insertAll(productAggregates: List<ProductAggregate>)
    fun saveAll(productAggregates: List<ProductAggregate>)
}
