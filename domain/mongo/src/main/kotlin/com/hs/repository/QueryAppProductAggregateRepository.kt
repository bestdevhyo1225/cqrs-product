package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType

interface QueryAppProductAggregateRepository {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate?
    fun findByProductIdAndTypeAndIsDisplay(
        productId: Long,
        type: ProductAggregateType,
        isDisplay: Boolean
    ): ProductAggregate?

    fun insert(productAggregate: ProductAggregate): ProductAggregate?
    fun save(productAggregate: ProductAggregate): ProductAggregate?
}
