package com.hs.repository

import com.hs.entity.ProductAggregate

interface QueryAppProductAggregateRepository {
    fun findByProductId(productId: Long): ProductAggregate?
    fun findByProductIdAndIsDisplay(productId: Long, isDisplay: Boolean): ProductAggregate?
    fun findAllByIsDisplay(isDisplay: Boolean, page: Int, pageSize: Int): Pair<List<ProductAggregate>, Long>
    fun insert(productAggregate: ProductAggregate): ProductAggregate?
    fun save(productAggregate: ProductAggregate): ProductAggregate?
}
