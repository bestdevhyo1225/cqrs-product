package com.hs.jpa.repository

import com.hs.jpa.entity.ProductAggregate

interface QueryAppProductAggregateRepository {
    fun findByProductId(productId: Long): ProductAggregate?
    fun findByProductIdAndIsDisplay(productId: Long, isDisplay: Boolean): ProductAggregate?
    fun findAllByIsDisplay(isDisplay: Boolean, page: Int, pageSize: Int): Pair<List<ProductAggregate>, Long>
    fun insert(productAggregate: ProductAggregate): ProductAggregate?
    fun save(productAggregate: ProductAggregate): ProductAggregate?
}
