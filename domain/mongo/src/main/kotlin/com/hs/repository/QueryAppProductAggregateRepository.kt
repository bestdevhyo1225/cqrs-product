package com.hs.repository

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface QueryAppProductAggregateRepository {
    fun findByProductIdAndType(productId: Long, type: ProductAggregateType): ProductAggregate?
    fun findByProductIdAndTypeAndIsDisplay(
        productId: Long,
        type: ProductAggregateType,
        isDisplay: Boolean
    ): ProductAggregate?

    fun findAllByTypeAndIsDisplay(
        type: ProductAggregateType,
        isDisplay: Boolean,
        pageable: Pageable
    ): Page<ProductAggregate>

    fun insert(productAggregate: ProductAggregate): ProductAggregate?
    fun save(productAggregate: ProductAggregate): ProductAggregate?
}
